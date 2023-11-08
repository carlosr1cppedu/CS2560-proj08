import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Parser {
    private static List<String> arithCmds = Arrays.asList("add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not");
    private String mCurrCommand = null;
    private Scanner mScanner = null;
    private String mArg0 = null;
    private String mArg1 = null;
    private String mArg2 = null;
    private String mCmdType = null;


    // opens input file/stream
    public Parser(File file) {
        try {
            mScanner = new Scanner(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    public boolean hasMoreCommands() {
        boolean hasMore = false;
        if (mScanner.hasNextLine()) {
            hasMore = true;
        }
        return hasMore;
    }


    // reads next command from input
    public void advance() {
        String strLine = mScanner.nextLine();
        while (strLine.equals("") || hasComments(strLine)) {
            if (hasComments(strLine)) {
                strLine = removeComments(strLine);
            }
            if (strLine.trim().equals("")) {
                strLine = mScanner.nextLine();
            }
        }


        mCurrCommand = strLine;
        String[] cmds = mCurrCommand.split(" ");
        mArg0 = cmds[0];
        if (cmds.length > 1) {
            mArg1 = cmds[1];
        }
        if (cmds.length > 2) {
            mArg2 = cmds[2];
        }


        if (mArg0.equals("push")) {
            mCmdType = "C_PUSH";
        } else if (mArg0.equals("pop")) {
            mCmdType = "C_POP";
        } else if (arithCmds.contains(mArg0)) {
            mCmdType = "C_ARITHMETIC";
        }
        else if (mArg0.equals("label")) {
            mCmdType = "C_LABEL";
        }
        else if (mArg0.equals("goto")) {
            mCmdType = "C_GOTO";
        }
        else if (mArg0.equals("if-goto")) {
            mCmdType = "C_IF";
        }
        else if (mArg0.equals("function")) {
            mCmdType = "C_FUNCTION";
        }
        else if (mArg0.equals("return")) {
            mCmdType = "C_RETURN";
        }
        else if (mArg0.equals("call")) {
            mCmdType = "C_CALL";
        }
     }




    // test if the line argument has comments in it
    private boolean hasComments(String strLine) {
        boolean bHasComments = false;
        if (strLine.contains("//")) {
            bHasComments = true;
        }
        return bHasComments;


    }


    // removes comments from a line
    private String removeComments(String strLine) {
        String strNoComments = strLine;
        if (hasComments(strLine)) {
            int offSet = strLine.indexOf("//");
            strNoComments = strLine.substring(0, offSet).trim();


        }
        return strNoComments;
    }


    // returns type for current VM command, C_ARITHMETIC returned for all arithmetic commands
    public String commandType() {
        return mCmdType;
    }


    public String arg1() {
        String strArg1 = null;
        if (mCmdType.equals("C_ARITHMETIC")) {
            strArg1 = mArg0;
        } else if (!(mCmdType.equals("C_RETURN"))) {
            strArg1 = mArg1;
        }
        return strArg1;
    }


    public int arg2() {
        int nArg2 = 0;
        if (mCmdType.equals("C_PUSH") || mCmdType.equals("C_POP") || mCmdType.equals("C_FUNCTION") || mCmdType.equals("C_CALL")) {
            nArg2 = Integer.parseInt(mArg2);
        }
        return nArg2;


    }


}
