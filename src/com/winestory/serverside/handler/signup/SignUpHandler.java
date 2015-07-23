package com.winestory.serverside.handler.signup;

import com.winestory.serverside.framework.database.PersistManager;
import com.winestory.serverside.framework.database.UserEntity;
import com.winestory.serverside.framework.response.HTTPResponder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;


public class SignUpHandler {
    public Logger logger = Logger.getLogger(SignUpHandler.class);
    private HTTPResponder httpResponder;

    public SignUpHandler(){
        this.httpResponder = new HTTPResponder();
    }

    public void signUp(ChannelHandlerContext ctx, FullHttpRequest req){
        logger.info("Method: signUp");

        logger.info("content:"+req.content().toString(CharsetUtil.UTF_8));

        String reqString = req.content().toString(CharsetUtil.UTF_8);

        PersistManager persistManager = new PersistManager();
        persistManager.save();

/*
        try {
            if(reqString!=null) {
                if(!reqString.isEmpty()) {
                    JSONObject incoming = new JSONObject(reqString);
                    JSONObject user = (JSONObject) incoming.get("user_sign_up");
                    String first_name = (String) user.get("first_name");
                    String last_name = (String) user.get("last_name");
                    String email = (String) user.get("email");



                    logger.info("TRY first name: " + first_name +
                                    " last_name: " + last_name +
                                    " email: " + email
                    );
                }else{
                    logger.info("this is normal. incoming request is empty");
                }
            }
        } catch (JSONException e) {
            logger.error("incoming reqString that caused error: "+reqString);
            e.printStackTrace();
        }
*/

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email","123456@sf.com");
            jsonObject.put("first_name","zhongqin");
            jsonObject.put("last_name", "ng");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        httpResponder.respond(ctx,req,jsonObject);


    }

}
