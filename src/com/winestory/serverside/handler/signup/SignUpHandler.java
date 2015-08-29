package com.winestory.serverside.handler.signup;

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


public class SignUpHandler {
    public Logger logger = Logger.getLogger(SignUpHandler.class);
    private HTTPResponder httpResponder;
    private PersistenceManager persistenceManager;

    public SignUpHandler(PersistenceManager persistenceManager){
        this.httpResponder = new HTTPResponder();
        this.persistenceManager=persistenceManager;
    }

    public void router(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest){

        Router router = new Router(fullHttpRequest.getUri(),persistenceManager);
        String action = router.getAction();
        Map<String,List<String>> params = router.getParameters();


        if(params.isEmpty()){
            logger.info("No Params");
        }else{

            if("SignUp".equals(action)){
                logger.info("Action = SignUp");
                signUp(ctx, fullHttpRequest);
                return;
            }

        }
    }

    public void signUp(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest){
        logger.info("Method: signUp");
        logger.info("content:"+fullHttpRequest.content().toString(CharsetUtil.UTF_8));

        String reqString = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
        UserVO userVO = null;

        try {
            if(reqString!=null) {
                if(!reqString.isEmpty()) {
                    JSONObject incoming = new JSONObject(reqString);
                    JSONObject data = (JSONObject) incoming.get("data");

                    String email = (String) data.get("email");
                    String password = (String) data.get("password");
                    String full_name = (String) data.get("full_name");


                    UserDAO userDAO = new UserDAO(persistenceManager);
                    boolean checkIfEmailIsTaken = userDAO.checkIfEmailIsTaken(email);
                    if(!checkIfEmailIsTaken){
                        String password_salt_hash;

                        PasswordHash passwordHash = new PasswordHash();
                        try {

                            password_salt_hash=passwordHash.createHash(password);
                            userVO = new UserVO(full_name,email,password_salt_hash);
                            userVO = userDAO.createNewUser(userVO);

                            UUIDGenerator uuidGenerator = new UUIDGenerator();
                            String new_session_id = uuidGenerator.getUUID();
                            logger.info("signup: new_session_id:" + new_session_id);
                            SessionDAO sessionDAO = new SessionDAO(persistenceManager);
                            SessionVO sessionVO = new SessionVO(new_session_id,userVO.getId());
                            sessionDAO.createSession(sessionVO);


                            httpResponder.respond2(channelHandlerContext,
                                    fullHttpRequest,
                                    new SignUpJSONHelper().getSession(sessionVO),
                                    new ReturnStatusHelper().getSEC103_SignUpSuccess(),
                                    userVO);

                            return;
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        }
                    }

                }else{
                    logger.info("INCOMING REQUEST IS EMPTY!");
                }
            }
        } catch (JSONException e) {
            logger.error("incoming reqString that caused error: "+reqString);
            e.printStackTrace();
        }

            httpResponder.respond2(channelHandlerContext,
                    fullHttpRequest,
                    null,
                    new ReturnStatusHelper().getSEC104_SignUpFail(),
                    null);
    }

}
