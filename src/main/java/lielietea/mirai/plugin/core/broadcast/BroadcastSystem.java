package lielietea.mirai.plugin.core.broadcast;

import lielietea.mirai.plugin.utils.IdentityUtil;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;

import java.util.Objects;

public class BroadcastSystem {

    public static void sendToAllGroups(MessageEvent event, String message) throws InterruptedException {
        sendToCertainGroups(event, message, event.getBot().getGroups());
    }

    public static void sendToCertainGroups(MessageEvent event, String message, ContactList<Group> groupContactList) throws InterruptedException {
        for (Group group : groupContactList) {
            Objects.requireNonNull(event.getBot().getGroup(group.getId())).sendMessage(message);
            Thread.sleep(3000);
        }
    }

    //测试广播消息
    public static void sendToAllGroups(FriendMessageEvent event) throws InterruptedException {
        String message = event.getMessage().contentToString();
        if (message.contains("/broadcast2g ") && IdentityUtil.isAdmin(event)) {
            message = message.replace("/broadcast2g ", "");
            sendToAllGroups(event, message);
            Objects.requireNonNull(event.getBot().getGroup(IdentityUtil.DevGroup.DEFAULT.getID())).sendMessage("群广播已完成。");
        }
    }

    public static void directlySendToGroup(FriendMessageEvent event) {
        String message = event.getMessage().contentToString();
        if (message.contains("/broadcast ") && IdentityUtil.isAdmin(event)) {
            String[] splitMessage = message.split(" ");
            if (splitMessage.length != 3) {
                event.getSubject().sendMessage("请使用空格分割/broadcast指示器、群号和消息");
                return;
            }
            if (!splitMessage[0].equals("/broadcast")) {
                event.getSubject().sendMessage("/broadcast指示器使用不正确");
                return;
            }
            Objects.requireNonNull(event.getBot().getGroup(Long.parseLong(splitMessage[1]))).sendMessage(splitMessage[2]);
        }
    }

    public static void sendToAllFriends(FriendMessageEvent event) throws InterruptedException {
        String message = event.getMessage().contentToString();
        if (message.contains("/broadcast2f ") && IdentityUtil.isAdmin(event)) {
            message = message.replace("/broadcast2f ", "");
            sendToCertainFriends(event, message, event.getBot().getFriends());
            Objects.requireNonNull(event.getBot().getGroup(IdentityUtil.DevGroup.DEFAULT.getID())).sendMessage("好友广播已完成。");
        }
    }

    public static void sendToCertainFriends(MessageEvent event, String message, ContactList<Friend> friendContactList) throws InterruptedException {
        for (Friend friend : friendContactList) {
            Objects.requireNonNull(event.getBot().getFriend(friend.getId())).sendMessage(message);
            Thread.sleep(3000);
        }
    }

    //broadcast helper
    public static void broadcastHelper(MessageEvent event) {
        if (event.getMessage().contentToString().contains("/broadcasthelper") && IdentityUtil.isAdmin(event)) {
            event.getSubject().sendMessage("/broadcast2f+空格+消息，发送给所有好友\n/broadcast2g+空格+消息，发送给所有群\n/broadcast+空格+群号+空格+消息，发送给指定群");
        }
    }
}
