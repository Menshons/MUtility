package com.mens.mutility.spigot.utils;

import com.mens.mutility.spigot.chat.PluginColors;
import com.mens.mutility.spigot.chat.json.JsonBuilder;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageList {
    private final int limit;
    private int index;
    private int maxPage;
    private final String title;
    private String command;
    private JsonBuilder jb;
    private final List<String> rows;
    private JsonBuilder head;
    private int extraDistance;
    private double titleLength;
    private double topLineFinalLength;

    public PageList(int limit, String title, String command) {
        this.limit = limit;
        this.title = title;
        this.command = command;
        index = 0;
        maxPage = 1;
        rows = new ArrayList<>();
        head = null;
        jb = new JsonBuilder();
        extraDistance = 0;
        titleLength = 0;
        topLineFinalLength = 0;
    }

    public int getLimit() {
        return limit;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public String getTitle() {
        return title;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getRows() {
        return rows;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public JsonBuilder getHead() {
        return head;
    }

    public void setHead(JsonBuilder head) {
        this.head = head;
    }

    public void add(String row) {
        if(getIndex() == getLimit()) {
            setMaxPage(getMaxPage() + 1);
            setIndex(0);
        } else {
            setIndex(getIndex() + 1);
        }
        rows.add(row);
    }

    public void clear() {
        index = 0;
        maxPage = 1;
        rows.clear();
    }

    public JsonBuilder getList(int pageNumber) {
        jb.clear();
        PluginColors colors = new PluginColors();
        StringBuilder sb = new StringBuilder();
        JsonBuilder firstPageHover = new JsonBuilder();
        JsonBuilder firstPage = new JsonBuilder("[");
        JsonBuilder previousPageHover = new JsonBuilder();
        JsonBuilder previousPage = new JsonBuilder("[");
        JsonBuilder nextPageHover = new JsonBuilder();
        JsonBuilder nextPage = new JsonBuilder("[");
        JsonBuilder lastPageHover = new JsonBuilder();
        JsonBuilder lastPage = new JsonBuilder("[");
        jb.addJsonSegment(getTopLine(getTitle(), colors.getSecondaryColorHEX()));

        // Head
        if(!getRows().isEmpty() && getHead() != null) {
            sb.append(",{\"text\":\" \"},");
            sb.append(getHead().getJsonSegments());
            jb.addJsonSegment(sb.toString());
        }

        boolean error = false;
        if(pageNumber > maxPage) {
            pageNumber = maxPage;
        }
        // Body
        sb = new StringBuilder();
        for (int i = pageNumber * limit - limit; i < pageNumber * limit; i++) {
            try {
                if(getRows().get(i) != null) {
                    sb.append(",{\"text\":\"\n \"},");
                }
                sb.append(getRows().get(i));
            } catch (IndexOutOfBoundsException e) {
                if(i == pageNumber * limit - limit) {
                    error = true;
                    if(pageNumber == 1 && getRows().isEmpty()) {
                        sb.append(",{\"text\":\"\n\"},");
                        sb.append("{\"text\":\"   Seznam je prázdný! \n\",");
                        sb.append("\"color\":\"");
                        sb.append(colors.getPrimaryColorHEX());
                        sb.append("\"}");
                    }
                }
                break;
            }
        }
        jb.addJsonSegment(sb.toString());
        sb = new StringBuilder();
        if(!error) {
            if(pageNumber != 1) {
                firstPageHover
                        .text(">> ")
                        .color(colors.getSecondaryColorHEX())
                        .text("První strana")
                        .color(colors.getPrimaryColorHEX())
                        .text(" <<")
                        .color(colors.getSecondaryColorHEX());
                firstPage
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, firstPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page 1")
                        .text("◀◀")
                        .color(colors.getPrimaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, firstPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page 1")
                        .text("]")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, firstPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page 1");

                previousPageHover
                        .text(">> ")
                        .color(colors.getSecondaryColorHEX())
                        .text("Předchozí strana")
                        .color(colors.getPrimaryColorHEX())
                        .text(" <<")
                        .color(colors.getSecondaryColorHEX());
                previousPage
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, previousPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page " + (pageNumber - 1))
                        .text("◀")
                        .color(colors.getPrimaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, previousPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page " + (pageNumber - 1))
                        .text("]")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, previousPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page " + (pageNumber - 1));
            } else {
                firstPageHover
                        .text(">> ")
                        .color(colors.getSecondaryColorHEX())
                        .text("Již se nacházíš na první straně!")
                        .color(colors.getSecondaryColorHEX())
                        .text(" <<")
                        .color(colors.getSecondaryColorHEX());
                firstPage
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, firstPageHover.toString(), true)
                        .text("◀◀")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, firstPageHover.toString(), true)
                        .text("]")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, firstPageHover.toString(), true);

                previousPageHover
                        .text(">> ")
                        .color(colors.getSecondaryColorHEX())
                        .text("Již se nacházíš na první straně!")
                        .color(colors.getSecondaryColorHEX())
                        .text(" <<")
                        .color(colors.getSecondaryColorHEX());
                previousPage
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, previousPageHover.toString(), true)
                        .text("◀")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, previousPageHover.toString(), true)
                        .text("]")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, previousPageHover.toString(), true);
            }
            if(pageNumber != getMaxPage()) {
                nextPageHover
                        .text(">> ")
                        .color(colors.getSecondaryColorHEX())
                        .text("Následující strana")
                        .color(colors.getPrimaryColorHEX())
                        .text(" <<")
                        .color(colors.getSecondaryColorHEX());
                nextPage
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, nextPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page " + (pageNumber + 1))
                        .text("▶")
                        .color(colors.getPrimaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, nextPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page " + (pageNumber + 1))
                        .text("]")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, nextPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page " + (pageNumber + 1));

                lastPageHover
                        .text(">> ")
                        .color(colors.getSecondaryColorHEX())
                        .text("Poslední strana")
                        .color(colors.getPrimaryColorHEX())
                        .text(" <<")
                        .color(colors.getSecondaryColorHEX());
                lastPage
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, lastPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page " + getMaxPage())
                        .text("▶▶")
                        .color(colors.getPrimaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, lastPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page " + getMaxPage())
                        .text("]")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, lastPageHover.toString(), true)
                        .clickEvent(JsonBuilder.ClickAction.RUN_COMMAND, getCommand() + " page " + getMaxPage());
            } else {
                nextPageHover
                        .text(">> ")
                        .color(colors.getSecondaryColorHEX())
                        .text("Již se nacházíš na poslední straně!")
                        .color(colors.getSecondaryColorHEX())
                        .text(" <<")
                        .color(colors.getSecondaryColorHEX());
                nextPage
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, nextPageHover.toString(), true)
                        .text("▶")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, nextPageHover.toString(), true)
                        .text("]")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, nextPageHover.toString(), true);

                lastPageHover
                        .text(">> ")
                        .color(colors.getSecondaryColorHEX())
                        .text("Již se nacházíš na poslední straně!")
                        .color(colors.getSecondaryColorHEX())
                        .text(" <<")
                        .color(colors.getSecondaryColorHEX());
                lastPage
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, lastPageHover.toString(), true)
                        .text("▶▶")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, lastPageHover.toString(), true)
                        .text("]")
                        .color(colors.getSecondaryColorHEX())
                        .hoverEvent(JsonBuilder.HoverAction.SHOW_TEXT, lastPageHover.toString(), true);
            }
        }
        jb.addJsonSegment(sb.toString());
        if(getMaxPage() > 1) {
            jb.addJsonSegment(getBottomLine(colors.getSecondaryColorHEX(), true, pageNumber, firstPage, previousPage, nextPage, lastPage));
        } else {
            jb.addJsonSegment(getBottomLine(colors.getSecondaryColorHEX(), false, pageNumber, firstPage, previousPage, nextPage, lastPage));
        }
        return jb;
    }

    private String getTopLine(String titleJson, String color) {
        MyStringUtils strUt = new MyStringUtils();
        StringBuilder sb = new StringBuilder();
        titleJson = titleJson.replace(" ", "");
        String title = "";
        for (int i = 40; i < titleJson.length(); i++) {
            if(titleJson.charAt(i) == '"') {
                break;
            }
            title += titleJson.charAt(i);
        }
        try {
            double spaceLength = 0.4921875;
            titleLength = strUt.getStringWidth(ChatColor.stripColor(title));
            double bottomLineLength = strUt.getStringWidth(ChatColor.stripColor(StringUtils.repeat("I", 50)));
            double topHalfLineLength = ((bottomLineLength - titleLength) - (2 * spaceLength)) / 2;
            int topLineNumber = (int) Math.round(topHalfLineLength / spaceLength);
            topLineFinalLength = 2 * topLineNumber * spaceLength + titleLength;
            double topLineLength = (2 * topLineNumber * spaceLength) + (2 * spaceLength) + titleLength;
            if(Math.abs(topLineLength - bottomLineLength) > Math.abs(topLineLength-(bottomLineLength + spaceLength))) {
                extraDistance = topLineLength > bottomLineLength? 1 : -1;
            }

            sb.append("{\"text\":\"\n\"},");
            sb.append("{\"text\":\"");
            sb.append(StringUtils.repeat(" ", topLineNumber));
            sb.append("\",\"strikethrough\":true,\"color\":\"");
            sb.append(color);
            sb.append("\"},");
            sb.append(titleJson);
            sb.append(",{\"text\":\"");
            sb.append(StringUtils.repeat(" ", topLineNumber));
            sb.append("\",\"strikethrough\":true,\"color\":\"");
            sb.append(color);
            sb.append("\"},");
            sb.append("{\"text\":\"\n\"}");
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getBottomLine(String color, boolean showPages, int pageNumber, JsonBuilder firstPage, JsonBuilder previousPage, JsonBuilder nextPage, JsonBuilder lastPage) {
        MyStringUtils strUt = new MyStringUtils();
        StringBuilder sb = new StringBuilder();
        PluginColors colors = new PluginColors();
        try {
            double spaceLength = 0.4921875;
            double titleLength = strUt.getStringWidth("(" + pageNumber + " | " + getMaxPage() + ")");
            double arrowLength = 8;
            int bottomLineSpaces = (int)Math.round((topLineFinalLength - (arrowLength + titleLength)) / 6 / spaceLength);
            double bottomFinalLength = bottomLineSpaces * spaceLength * 6 + arrowLength + titleLength;
            if(showPages) {
                extraDistance = (int)Math.round((topLineFinalLength - bottomFinalLength) / spaceLength);
                String pagesJson = new JsonBuilder("(")
                        .color(color)
                        .text(String.valueOf(pageNumber))
                        .color(colors.getPrimaryColorHEX())
                        .text(" | ")
                        .color(color)
                        .text(String.valueOf(getMaxPage()))
                        .color(colors.getPrimaryColorHEX())
                         .text(")")
                        .color(color)
                        .getJsonSegments();
                sb.append("{\"text\":\"\n\"},");
                sb.append("{\"text\":\"");
                sb.append(StringUtils.repeat(" ", bottomLineSpaces));
                sb.append("\",\"strikethrough\":true,\"color\":\"");
                sb.append(color);
                sb.append("\"},");
                sb.append(firstPage.getJsonSegments());
                sb.append(",{\"text\":\"");
                sb.append(StringUtils.repeat(" ", bottomLineSpaces));
                sb.append("\",\"strikethrough\":true,\"color\":\"");
                sb.append(color);
                sb.append("\"},");
                sb.append(previousPage.getJsonSegments());
                sb.append(",{\"text\":\"");
                sb.append(StringUtils.repeat(" ", bottomLineSpaces));
                sb.append("\",\"strikethrough\":true,\"color\":\"");
                sb.append(color);
                sb.append("\"},");
                sb.append(pagesJson);
                sb.append(",{\"text\":\"");
                sb.append(StringUtils.repeat(" ", bottomLineSpaces));
                sb.append("\",\"strikethrough\":true,\"color\":\"");
                sb.append(color);
                sb.append("\"},");
                sb.append(nextPage.getJsonSegments());
                sb.append(",{\"text\":\"");
                sb.append(StringUtils.repeat(" ", bottomLineSpaces));
                sb.append("\",\"strikethrough\":true,\"color\":\"");
                sb.append(color);
                sb.append("\"},");
                sb.append(lastPage.getJsonSegments());
                sb.append(",{\"text\":\"");
                sb.append(StringUtils.repeat(" ", (bottomLineSpaces + extraDistance)));
                sb.append("\",\"strikethrough\":true,\"color\":\"");
                sb.append(color);
                sb.append("\"},");
                sb.append("{\"text\":\"\n\"}");
            } else {
                sb.append("{\"text\":\"\n\"},");
                sb.append("{\"text\":\"");
                sb.append(StringUtils.repeat(" ", (50 + extraDistance)));
                sb.append("\",\"strikethrough\":true,\"color\":\"");
                sb.append(color);
                sb.append("\"},");
                sb.append("{\"text\":\"\n\"}");
            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
