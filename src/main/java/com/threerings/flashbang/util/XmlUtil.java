//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.util;

import com.google.gwt.xml.client.*;
import com.google.gwt.xml.client.impl.DOMParseException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class XmlUtil
{
    public static Node parseString (String xmlString)
        throws XmlReadException
    {
        return parse(xmlString);
    }

    /**
     * Returns a list of children elements that match the given path.
     * You can use '/' and '*' in the path, e.g. "Animals/Monkeys/*"
     */
    public static List<Node> getChildren (Node node, String path)
    {
        // This is pretty inefficient!

        List<Node> matching = null;
        int nextSeparator = (path != null ? path.indexOf('/') : -1);
        String curName = (nextSeparator > 0 ? path.substring(0, nextSeparator) : path);
        Preconditions.checkState(curName.length() > 0);

        if (nextSeparator < 0) {
            // base case
            NodeList kids = node.getChildNodes();
            matching = Lists.newArrayListWithCapacity(kids.getLength());
            for (int ii = 0; ii < kids.getLength(); ii++) {
                Node kid = kids.item(ii);
                if (kid instanceof Element &&
                    ("*".equals(curName) || ((Element)kid).getTagName().equals(curName))) {
                    matching.add(kids.item(ii));
                }
            }

        } else {
            String remainingPath = path.substring(nextSeparator + 1);
            Preconditions.checkState(remainingPath.length() > 0);
            for (Node childNode : getChildren(node, curName)) {
                // recurse
                List<Node> children = getChildren(childNode, remainingPath);
                if (matching == null) {
                    matching = children;
                } else {
                    matching.addAll(children);
                }
            }
        }

        if (matching == null) {
            matching = Collections.emptyList();
        }

        return matching;
    }

    /**
     * Returns all of node's children whose type is Element
     */
    public static List<Node> getChildren (Node node)
    {
        return getChildren(node, "*");
    }

    /**
     * Returns true if the node has at least one child with the specified name
     */
    public static boolean hasChild (Node node, String name)
    {
        Node kid = node.getFirstChild();
        while (kid != null) {
            if (kid instanceof Element &&
                ("*".equals(name) || name == null || ((Element)kid).getTagName().equals(name))) {
                return true;
            }
            kid = kid.getNextSibling();
        }
        return false;
    }

    public static Node getSingleChild (Node node, String name)
        throws XmlReadException
    {
        List<Node> children = getChildren(node, name);
        if (children.size() != 1) {
            throw new XmlReadException(
                "There must be exactly one '" + name + "' child (found " + children.size() + ")",
                node);
        }
        return children.get(0);
    }

    public static Node getSingleChild (Node node)
        throws XmlReadException
    {
        return getSingleChild(node, "*");
    }

    public static boolean hasAttr (Node node, String name)
    {
        return (getAttr(node, name, null) != null);
    }

    public static String getAttr (Node node, String name)
        throws XmlReadException
    {
        return getAttr(node, name, null, false);
    }

    public static String getAttr (Node node, String name, String defaultVal)
    {
        try {
            return getAttr(node, name, defaultVal, true);
        } catch (XmlReadException e) {
            // this will never happen - defaultVal will be returned from the above function
            return defaultVal;
        }
    }

    public static int getIntValue (Node node)
        throws XmlReadException
    {
        try {
            return parseInt(node.getNodeValue());
        } catch (NumberFormatException e) {
            throw new XmlReadException("Error reading value from '" + node.getNodeName() +
                    "': could not convert '" + node.getNodeValue() + "' to int", node);
        }
    }

    public static int getIntAttr (Node node, String name)
        throws XmlReadException
    {
        return getIntAttr(node, name, 0, false);
    }

    public static int getIntAttr (Node node, String name, int defaultVal)
        throws XmlReadException
    {
        return getIntAttr(node, name, defaultVal, true);
    }

    public static long getLongAttr (Node node, String name)
        throws XmlReadException
    {
        return getLongAttr(node, name, 0, false);
    }

    public static long getLongAttr (Node node, String name, long defaultVal)
        throws XmlReadException
    {
        return getLongAttr(node, name, defaultVal, true);
    }

    public static int getUintAttr (Node node, String name)
        throws XmlReadException
    {
        return getUintAttr(node, name, 0, false);
    }

    public static int getUintAttr (Node node, String name, int defaultVal)
        throws XmlReadException
    {
        return getUintAttr(node, name, defaultVal, true);
    }

    public static float getFloatValue (Node node)
        throws XmlReadException
    {
        try {
            return Float.parseFloat(node.getNodeValue());
        } catch (NumberFormatException e) {
            throw new XmlReadException("Error reading value from '" + node.getNodeName() +
                    "': could not convert '" + node.getNodeValue() + "' to float", node);
        }
    }

    public static float getFloatAttr (Node node, String name)
        throws XmlReadException
    {
        return getFloatAttr(node, name, 0, false);
    }

    public static float getFloatAttr (Node node, String name, float defaultVal)
        throws XmlReadException
    {
        return getFloatAttr(node, name, defaultVal, true);
    }

    public static boolean getBooleanAttr (Node node, String name)
        throws XmlReadException
    {
        return getBooleanAttr(node, name, false, false);
    }

    public static boolean getBooleanAttr (Node node, String name, boolean defaultVal)
        throws XmlReadException
    {
        return getBooleanAttr(node, name, defaultVal, true);
    }

    public static <T extends Enum<?>> T getEnumAttr (Node node, String name, Class<T> clazz)
        throws XmlReadException
    {
        return getEnumAttr(node, name, clazz, null, false);
    }

    public static <T extends Enum<?>> T getEnumAttr (Node node, String name, Class<T> clazz,
        T defaultVal)
        throws XmlReadException
    {
        return getEnumAttr(node, name, clazz, defaultVal, true);
    }

    /**
     * Parses a string in the form "FOO,BAR,MONKEY" into a Set of Enums
     */
    public static <T extends Enum<?>> Set<T> getEnumSetAttr (Node node, String name, Class<T> clazz)
        throws XmlReadException
    {
        try {
            return XmlUtil.parseEnumSet(getAttr(node, name, null, false), clazz);
        } catch (RuntimeException e) {
            throw new XmlReadException("Error reading attribute '" + name + ": " + e.getMessage(),
                node);
        }
    }

    protected static String getAttr (Node node, String name, String defaultVal,
        boolean useDefaultVal)
        throws XmlReadException
    {
        Attr attr = (Attr)node.getAttributes().getNamedItem(name);
        if (attr == null) {
            if (useDefaultVal) {
                return defaultVal;
            } else {
                throw new XmlReadException("Error reading attribute '" + name
                    + "': attribute does not exist", node);
            }
        }
        return attr.getValue();
    }

    protected static int getIntAttr (Node node, String name, int defaultVal, boolean useDefaultVal)
        throws XmlReadException
    {
        String val = getAttr(node, name, "" + defaultVal, useDefaultVal);
        try {
            return parseInt(val);
        } catch (NumberFormatException e) {
            throw new XmlReadException("Error reading attribute '" + name +
                    "': could not convert '" + val + "' to int", node);
        }
    }

    protected static long getLongAttr (Node node, String name, long defaultVal, boolean useDefaultVal)
        throws XmlReadException
    {
        String val = getAttr(node, name, "" + defaultVal, useDefaultVal);
        try {
            return parseLong(val);
        } catch (NumberFormatException e) {
            throw new XmlReadException("Error reading attribute '" + name +
                    "': could not convert '" + val + "' to long", node);
        }
    }

    protected static int getUintAttr (Node node, String name, int defaultVal,
        boolean useDefaultVal)
        throws XmlReadException
    {
        String val = getAttr(node, name, "" + defaultVal, useDefaultVal);
        try {
            return parseUnsignedInt(val);
        } catch (NumberFormatException e) {
            throw new XmlReadException("Error reading attribute '" + name +
                    "': could not convert '" + val + "' to unsigned int", node);
        }
    }

    protected static float getFloatAttr (Node node, String name, float defaultVal,
        boolean useDefaultVal)
        throws XmlReadException
    {
        String val = getAttr(node, name, "" + defaultVal, useDefaultVal);
        try {
            return parseFloat(val);
        } catch (NumberFormatException e) {
            throw new XmlReadException("Error reading attribute '" + name +
                    "': could not convert '" + val + "' to float", node);
        }
    }

    protected static boolean getBooleanAttr (Node node, String name, boolean defaultVal,
        boolean useDefaultVal)
        throws XmlReadException
    {
        String val = getAttr(node, name, "" + defaultVal, useDefaultVal);
        try {
            return parseBoolean(val);
        } catch (NumberFormatException e) {
            throw new XmlReadException("Error reading attribute '" + name +
                    "': could not convert '" + val + "' to boolean", node);
        }
    }

    protected static <T extends Enum<?>> T getEnumAttr (Node node, String name, Class<T> clazz,
        T defaultVal, boolean useDefaultVal)
        throws XmlReadException
    {
        String val = getAttr(node, name, (defaultVal != null ? defaultVal.name() : null),
            useDefaultVal);
        if (val == null) {
            return null;
        }

        try {
            return parseEnum(val, clazz);
        } catch (RuntimeException e) {
            throw new XmlReadException("Error reading attribute '" + name +
                "': could not convert '" + val + "' to " + clazz);
        }
    }

    public static int parseInt (String val)
        throws NumberFormatException
    {
        if (val.startsWith("0x")) {
            return Integer.parseInt(val.substring(2), 16);
        } else if (val.startsWith("0") && val.length() > 1) {
            return Integer.parseInt(val.substring(1), 8);
        } else {
            return Integer.parseInt(val);
        }
    }

    public static int parseUnsignedInt (String val)
        throws NumberFormatException
    {
        if (val.indexOf('-') == 0) {
            throw new NumberFormatException("Error parsing '" + val + "': '-' not allowed");
        } else {
            return parseInt(val);
        }
    }

    public static long parseLong (String val)
        throws NumberFormatException
    {
        return Long.parseLong(val);
    }

    public static float parseFloat (String val)
        throws NumberFormatException
    {
        return Float.parseFloat(val);
    }

    public static <T extends Enum<?>> T parseEnum (String val, Class<T> enumType)
    {
        for (T enumVal : enumType.getEnumConstants()) {
            if (enumVal.name().equals(val)) {
                return enumVal;
            }
        }
        throw new RuntimeException("Not a valid enum name :'" + val + "'");
    }

    public static <T extends Enum<?>> Set<T> parseEnumSet (String str, Class<T> clazz)
    {
        if (str.length() == 0) {
            return Sets.newHashSet();
        }

        Set<T> enumSet = Sets.newHashSet();
        for (String enumName : StringUtil.split(str, ",")) {
            boolean foundEnumVal = false;
            for (T enumVal : clazz.getEnumConstants()) {
                if (enumVal.name().equals(enumName)) {
                    enumSet.add(enumVal);
                    foundEnumVal = true;
                    break;
                }
            }

            if (!foundEnumVal) {
                throw new RuntimeException("Not a valid enum name :'" + enumName + "'");
            }
        }
        return enumSet;
    }

    /**
     * Parses the given String into a boolean. Any capitalization of "true" parses to true;
     * any capitalization of "false" parses to false; anything else causes a NumberFormatException.
     * (Boolean.parseBoolean parses all non-"true" Strings to false, which isn't what we want.)
     */
    public static boolean parseBoolean (String val)
        throws NumberFormatException
    {
        if ("false".equalsIgnoreCase(val)) {
            return false;
        } else if ("true".equalsIgnoreCase(val)) {
            return true;
        } else {
            throw new NumberFormatException("Can't convert '" + val + "' to boolean");
        }
    }

    protected static Node parse (String str)
        throws XmlReadException
    {
        try {
            return XMLParser.parse(str);
        } catch (DOMParseException e) {
            throw new XmlReadException("Error parsing XML", e);
        }
    }

    // Stolen from Samskivert.
    // TODO: move this elsewhere!
    protected static class StringUtil {
        /**
         * Splits the supplied string into components based on the specified separator string.
         */
        public static String[] split (String source, String sep)
        {
            // handle the special case of a zero-component source
            if (isBlank(source)) {
                return new String[0];
            }

            int tcount = 0, tpos = -1, tstart = 0;

            // count up the number of tokens
            while ((tpos = source.indexOf(sep, tpos+1)) != -1) {
                tcount++;
            }

            String[] tokens = new String[tcount+1];
            tpos = -1; tcount = 0;

            // do the split
            while ((tpos = source.indexOf(sep, tpos+1)) != -1) {
                tokens[tcount] = source.substring(tstart, tpos);
                tstart = tpos+1;
                tcount++;
            }

            // grab the last token
            tokens[tcount] = source.substring(tstart);

            return tokens;
        }

        /**
         * @return true if the string is null or consists only of whitespace, false otherwise.
         */
        public static boolean isBlank (String value)
        {
            for (int ii = 0, ll = (value == null) ? 0 : value.length(); ii < ll; ii++) {
                if (!Character.isWhitespace(value.charAt(ii))) {
                    return false;
                }
            }
            return true;
        }
    }
}
