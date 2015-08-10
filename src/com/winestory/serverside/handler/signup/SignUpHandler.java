package com.winestory.serverside.handler.signup;

import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.database.DAO.UserDAO;
import com.winestory.serverside.framework.database.PersistenceManager;
import com.winestory.serverside.framework.response.HTTPResponder;
import com.winestory.serverside.framework.security.PasswordHash;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class SignUpHandler {
    public Logger logger = Logger.getLogger(SignUpHandler.class);
    private HTTPResponder httpResponder;
    private PersistenceManager persistenceManager;

    public SignUpHandler(PersistenceManager persistenceManager){
        this.httpResponder = new HTTPResponder();
        this.persistenceManager=persistenceManager;
    }

    public void signUp(ChannelHandlerContext ctx, FullHttpRequest req){
        logger.info("Method: signUp");
        logger.info("content:"+req.content().toString(CharsetUtil.UTF_8));

        String reqString = req.content().toString(CharsetUtil.UTF_8);
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
                            userDAO.createNewUser(userVO);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        }
                    }

                    respond(ctx, req, userVO);


                }else{
                    logger.info("INCOMING REQUEST IS EMPTY!");
                }
            }
        } catch (JSONException e) {
            logger.error("incoming reqString that caused error: "+reqString);
            e.printStackTrace();
        }
    }

    private void respond(ChannelHandlerContext ctx, FullHttpRequest req, UserVO userVO){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("data","dummy");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        httpResponder.respond(ctx,req,jsonObject,userVO);

    }

}
