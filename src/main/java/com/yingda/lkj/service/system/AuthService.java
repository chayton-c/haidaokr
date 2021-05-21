package com.yingda.lkj.service.system;

import com.yingda.lkj.beans.entity.system.Menu;
import com.yingda.lkj.beans.entity.system.User;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @author hood  2019/12/18
 */
public interface AuthService {

    void addAuth(String roleId, String menuId);

    void removeAuth(String roleId, String menuId);

    void updateAuthBackstage(String roleId, List<String> menuIds);

    /**
     * 对应角色是否允许访问该页面
     */
    boolean hasAccess(String roleId, String menuId);

    List<Menu> getValuableMenus(User user);

    List<Menu> getVueValuableMenus(User user);


    @Async("customServiceExecutor")
    default void ttt(int i) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("finished: " + i);
    }
}
