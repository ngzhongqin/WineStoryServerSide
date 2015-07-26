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


        try {
            if(reqString!=null) {
                if(!reqString.isEmpty()) {
                    JSONObject incoming = new JSONObject(reqString);
                    JSONObject data = (JSONObject) incoming.get("data");

                    String email = (String) data.get("email");
                    String password = (String) data.get("password");
                    String full_name = (String) data.get("full_name");


                    persistManager.save(email);
                    logger.info("TRY first name: " + password +
                                    " last_name: " + full_name +
                                    " email: " + email
                    );

                    respond(ctx,req);


                }else{
                    logger.info("this is normal. incoming request is empty");
                }
            }
        } catch (JSONException e) {
            logger.error("incoming reqString that caused error: "+reqString);
            e.printStackTrace();
        }




    }

    private void respond(ChannelHandlerContext ctx, FullHttpRequest req){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email","123456@sf.com");
            jsonObject.put("full_name","Zhong Qin");
            jsonObject.put("password", "password_input");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        httpResponder.respond(ctx,req,jsonObject);

    }

}
