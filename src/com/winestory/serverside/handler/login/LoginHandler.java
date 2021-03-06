package com.winestory.serverside.handler.login;

import com.winestory.serverside.framework.JSONHelper;
import com.winestory.serverside.framework.UUIDGenerator;
import com.winestory.serverside.framework.VO.SessionVO;
import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.database.DAO.SessionDAO;
import com.winestory.serverside.framework.database.DAO.UserDAO;
import com.winestory.serverside.framework.database.PersistenceManager;
import com.winestory.serverside.framework.helper.ReturnStatusHelper;
import com.winestory.serverside.framework.response.HTTPResponder;
import com.winestory.serverside.framework.security.PasswordHash;
import com.winestory.serverside.router.Router;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;


public class LoginHandler {
    public Logger logger = Logger.getLogger(LoginHandler.class);
    private HTTPResponder httpResponder;
    private LoginJSONHelper loginJSONHelper;
    private PersistenceManager persistenceManager;

    public LoginHandler(PersistenceManager persistenceManager){
        this.httpResponder = new HTTPResponder();
        this.loginJSONHelper = new LoginJSONHelper();
        this.persistenceManager=persistenceManager;
    }

    public void router(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest){

        Router router = new Router(fullHttpRequest.getUri(),persistenceManager);
        String action = router.getAction();
        Map<String,List<String>> params = router.getParameters();
        String session_id = router.getSession();

        if(params.isEmpty()){
            logger.info("No Params");
        }else{

            if("Login".equals(action)){
                logger.info("Action = Login");
                login(ctx, fullHttpRequest);
                return;
            }

            if("Logout".equals(action)){
                logger.info("Action = Logout");
                logout(ctx, fullHttpRequest,session_id);
                return;
            }

        }
    }

    private void login(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest){
        logger.info("Method: login");
        logger.info("content:"+fullHttpRequest.content().toString(CharsetUtil.UTF_8));

        String reqString = fullHttpRequest.content().toString(CharsetUtil.UTF_8);

        try {
            if(reqString!=null) {
                if(!reqString.isEmpty()) {
                    JSONObject incoming = new JSONObject(reqString);

                    JSONHelper jsonHelper = new JSONHelper();

                    JSONObject data = jsonHelper.getJSONObject(incoming,"data");
                    String email = jsonHelper.getString(data, "email");
                    String password = jsonHelper.getString(data,"password");

                    UserDAO userDAO = new UserDAO(persistenceManager);
                    UserVO userVO = userDAO.getUser(email);
                    logger.info("login: userVO:"+userVO);
                    if(userVO!=null){
                        logger.info("login: userVO: "+userVO.getFull_name());
                    }
                    //check if there is such user
                    if(userVO!=null){
                        boolean isPasswordCorrect = false;
                        try {
                            //validate password
                            PasswordHash passwordHash = new PasswordHash();
                            isPasswordCorrect = passwordHash.validatePassword(password,userVO.getPassword_salt_hash());
                            logger.info("login: email:"+email+" isPasswordCorrect="+isPasswordCorrect);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        }

                        //if Password is correct
                        if(isPasswordCorrect){
                            UUIDGenerator uuidGenerator = new UUIDGenerator();
                            String new_session_id = uuidGenerator.getUUID();
                            logger.info("login: new_session_id:" + new_session_id);
                            SessionDAO sessionDAO = new SessionDAO(persistenceManager);
                            SessionVO sessionVO = new SessionVO(new_session_id,userVO.getId());
                            sessionDAO.createSession(sessionVO);


                            httpResponder.respond2(channelHandlerContext, fullHttpRequest,
                                    loginJSONHelper.getJSONLoginSuccess(sessionVO),
                                    new ReturnStatusHelper().getSEC100_LoginSuccess(),
                                    null
                            );

                            //httpResponder.respond(ctx,req,loginJSONHelper.getJSONLoginSuccess(sessionVO),userVO);
                            return;
                        }

                    }
                    httpResponder.respond2(channelHandlerContext, fullHttpRequest,
                            null,
                            new ReturnStatusHelper().getSEC101_LoginFail(),
                            null);

//                    httpResponder.respond(channelHandlerContext,fullHttpRequest,loginJSONHelper.getJSONLoginFailed(),userVO);

                }else{
                    logger.info("INCOMING REQUEST IS EMPTY!");
                }
            }
        } catch (JSONException e) {
            logger.error("incoming reqString that caused error: "+reqString);
            e.printStackTrace();
        }
    }


    private void logout(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest, String session_id){
        logger.info("Method: logout");
        logger.info("content:"+fullHttpRequest.content().toString(CharsetUtil.UTF_8));

        String reqString = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
        SessionDAO sessionDAO = new SessionDAO(persistenceManager);
        sessionDAO.destroySession(session_id);

        httpResponder.respond2(channelHandlerContext,fullHttpRequest,
                null,
                new  ReturnStatusHelper().getSEC102_LogoutSuccess(),
                null);
//        httpResponder.respond(ctx,req,loginJSONHelper.getJSONLogout(),null);

    }

}


