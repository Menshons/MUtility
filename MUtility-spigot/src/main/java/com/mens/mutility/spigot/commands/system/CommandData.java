package com.mens.mutility.spigot.commands.system;

import com.mens.mutility.spigot.commands.system.enums.ArgumentTypes;
import com.mens.mutility.spigot.commands.system.enums.CommandExecutors;
import com.mens.mutility.spigot.commands.system.enums.TabCompleterTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandData {
    String commandName;
    ArgumentTypes argumentType;
    String subcommand;
    TabCompleterTypes tc;
    String tcCustom;
    String permission;
    CommandExecutors executor;
    Consumer<String[]> execute;
    List<CommandData> next;

    public CommandData(String commandName) {
        this.commandName = commandName;
        next = new ArrayList<>();
    }

    public CommandData(String commandName, String permission, CommandExecutors executor, Consumer<String[]> execute) {
        this.commandName = commandName;
        this.permission = permission;
        this.executor = executor;
        this.execute = execute;
        next = new ArrayList<>();
    }

    public CommandData(ArgumentTypes argumentType, TabCompleterTypes tc, String permission) {
        this.argumentType = argumentType;
        this.tc = tc;
        this.permission = permission;
        next = new ArrayList<>();
    }

    public CommandData(ArgumentTypes argumentType, TabCompleterTypes tc, String tcCustom, String permission) {
        this.argumentType = argumentType;
        this.tc = tc;
        this.tcCustom = tcCustom;
        this.permission = permission;
        next = new ArrayList<>();
    }

    public CommandData(ArgumentTypes argumentType, TabCompleterTypes tc, String permission, CommandExecutors executor, Consumer<String[]> execute) {
        this.argumentType = argumentType;
        this.tc = tc;
        this.permission = permission;
        this.executor = executor;
        this.execute = execute;
        next = new ArrayList<>();
    }

    public CommandData(ArgumentTypes argumentType, TabCompleterTypes tc, String tcCustom, String permission, CommandExecutors executor, Consumer<String[]> execute) {
        this.argumentType = argumentType;
        this.tc = tc;
        this.tcCustom = tcCustom;
        this.permission = permission;
        this.executor = executor;
        this.execute = execute;
        next = new ArrayList<>();
    }

    public CommandData(ArgumentTypes argumentType, String subcommand, TabCompleterTypes tc) {
        this.argumentType = argumentType;
        this.subcommand= subcommand;
        this.tc = tc;
        next = new ArrayList<>();
    }

    public CommandData(ArgumentTypes argumentType, String subcommand, TabCompleterTypes tc, String permission) {
        this.argumentType = argumentType;
        this.subcommand= subcommand;
        this.tc = tc;
        this.permission = permission;
        next = new ArrayList<>();
    }

    public CommandData(ArgumentTypes argumentType, String subcommand, TabCompleterTypes tc, String tcCustom, String permission) {
        this.argumentType = argumentType;
        this.subcommand= subcommand;
        this.tc = tc;
        this.tcCustom = tcCustom;
        this.permission = permission;
        next = new ArrayList<>();
    }

    public CommandData(ArgumentTypes argumentType, String subcommand, TabCompleterTypes tc, String permission, CommandExecutors executor, Consumer<String[]> execute) {
        this.argumentType = argumentType;
        this.subcommand = subcommand;
        this.tc = tc;
        this.permission = permission;
        this.executor = executor;
        this.execute = execute;
        next = new ArrayList<>();
    }

    public CommandData(ArgumentTypes argumentType, String subcommand, TabCompleterTypes tc, String tcCustom, String permission, CommandExecutors executor, Consumer<String[]> execute) {
        this.argumentType = argumentType;
        this.subcommand = subcommand;
        this.tc = tc;
        this.tcCustom = tcCustom;
        this.permission = permission;
        this.executor = executor;
        this.execute = execute;
        next = new ArrayList<>();
    }

    public ArgumentTypes getArgumentType() {
        return argumentType;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getSubcommand() {
        return subcommand;
    }

    public TabCompleterTypes getTc() {
        return tc;
    }

    public String getTcCustom() {
        return tcCustom;
    }

    public String getPermission() {
        return permission;
    }

    public Consumer<String[]> getExecute() {
        return execute;
    }

    public void link(CommandData data) {
        next.add(data);
    }
}
