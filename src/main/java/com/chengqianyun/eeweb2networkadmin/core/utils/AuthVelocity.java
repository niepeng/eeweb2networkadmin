package com.chengqianyun.eeweb2networkadmin.core.utils;

//import com.hd.entitys.leopard.console.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
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
        MenuEnum menuEnum = MenuEnum.findMenu(auth);
        ConsoleLoginAccount loginAccount = HttpSessionUtil.getLoginSession();
        if(menuEnum == null || loginAccount == null || loginAccount == null || !menuEnum.hasPermission(loginAccount.getRoleId())) {
            return false;
        }

        node.jjtGetChild(1).render(internalContextAdapter,writer);
        return true;
    }
}
