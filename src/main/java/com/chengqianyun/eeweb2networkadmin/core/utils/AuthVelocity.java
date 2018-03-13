package com.chengqianyun.eeweb2networkadmin.core.utils;

//import com.hd.entitys.leopard.console.ConsoleLoginAccount;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

/**
 * @version 1.0
 * @date 2018/1/11
 */
public class AuthVelocity extends Directive{

    @Override
    public String getName() {
        return "auth";
    }

    @Override
    public int getType() {
        return BLOCK;
    }

    @Override
    public boolean render(InternalContextAdapter internalContextAdapter, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        String auth = String.valueOf(node.jjtGetChild(0).value(internalContextAdapter));
//        ConsoleLoginAccount session = HttpSessionUtil.getLoginSession();
//        if (session!=null&&session.getAuthCodes()!=null){
//            if (session.getAuthCodes().contains(AuthUtil.wrapComma(auth))){
//                node.jjtGetChild(1).render(internalContextAdapter,writer);
//            }
//        }
        return true;
    }
}
