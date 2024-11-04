package com.example.demo.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Logic {
}

abstract class Entity implements Serializable {

    protected final String DATE;
    protected final UUID ID;

    public Entity(UUID ID) {
        this.DATE = getNowDate();
        this.ID = ID;
    }

    public static String getNowDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
        return now.format(formatter);
    }

    public static UUID generateUUID() {
        return UUID.randomUUID();
    }

    public static Entity find(HashMap<UUID, Entity> map, UUID id) {
        return map.get(id);
    }

    public static User find(HashMap<UUID, Entity> map, String login, String password) {
        for (Entity entity : map.values()) {
            if (entity instanceof User) {
                User user = (User) entity;
                if (user.getLOGIN().equals(login) && user.getPassword().equals(password)) {
                    return user;
                }
            }
        }
        return null;
    }

    public static User find(HashMap<UUID, Entity> map, String login) {
        for (Entity entity : map.values()) {
            if (entity instanceof User) {
                User user = (User) entity;
                if (user.getLOGIN().equals(login)) {
                    return user;
                }
            }
        }
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(ID, entity.ID) && Objects.equals(DATE, entity.DATE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(DATE, ID);
    }

}

class User extends Entity {

    private String name, password;
    private List<UUID> dialogs, images;
    private final String LOGIN;
    private int age;

    public User(UUID ID, String name, String password, String LOGIN, int age) {
        super(ID);
        this.name = name;
        this.password = password;
        this.LOGIN = LOGIN;
        this.age = age;
    }

    public List<UUID> getImages() {return images;}
    public void setImages(List<UUID> images) {this.images = images;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public List<UUID> getDialogs() {return dialogs;}
    public void setDialogs(List<UUID> dialogs) {this.dialogs = dialogs;}
    public String getLOGIN() {return LOGIN;}
    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}
}

class Dialog extends Entity {
    private List<Message> messages;

    public Dialog(UUID ID) {
        super(ID);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}

abstract class Logger {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String ANSI_RESET = "\u001B[0m";  // Сбрасывает цвет текста и фона к значениям по умолчанию.
    public static final String ANSI_RED = "\u001B[31m";   // Красный цвет текста (danger).
    public static final String ANSI_GREEN = "\u001B[32m"; // Зеленый цвет текста (successfull).
    public static final String ANSI_YELLOW = "\u001B[33m";// Желтый цвет текста (level).
    public static final String ANSI_PURPLE = "\u001B[35m";// Фиолетовый цвет текста (methodName/className).
    public static final String ANSI_CYAN = "\u001B[36m";  // Голубой цвет текста (timestamp).
    public static final String ANSI_BRIGHT_MAGENTA = "\u001B[95m"; // Ярко-пурпурный цвет (elapsedTime).

    public static void successfully(String message, long elapsedTime) {
        log(message, elapsedTime, ANSI_GREEN, "SUCCESS");
    }

    public static void danger(String message, long elapsedTime) {
        log(message, elapsedTime, ANSI_RED, "DANGER");
    }

    private static void log(String message, long elapsedTime, String color, String level) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[3]; // Индекс 3 соответствует вызывающему методу
        String className = caller.getClassName();
        String methodName = caller.getMethodName();
        String lineNumber = String.valueOf(caller.getLineNumber());
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        System.out.print(ANSI_CYAN + "[" + timestamp + "] " + ANSI_RESET);
        System.out.print(ANSI_PURPLE + "[" + className + "." + methodName + ":" + lineNumber + "] " + ANSI_RESET);
        System.out.print(ANSI_YELLOW + "[" + level + "] " + ANSI_RESET);
        System.out.println(color + "[" + message + "] " + ANSI_BRIGHT_MAGENTA + "[Time: " + elapsedTime + " ms]" + ANSI_RESET);
    }
}

class Message extends Entity {
    private String text;
    private final String SENDER;

    public Message(UUID ID, String SENDER, String text) {
        super(ID);
        this.SENDER = SENDER;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSENDER() {
        return SENDER;
    }
}

class Image extends Message {
    private final byte[] IMAGE;

    public Image(UUID ID, String SENDER, String text, byte[] IMAGES) {
        super(ID, SENDER, text);
        this.IMAGE = IMAGES;
    }

    public byte[] getIMAGES() {
        return IMAGE;
    }
}
