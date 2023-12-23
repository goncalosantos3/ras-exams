package ras.exams.exams.data;

public class InvalidQuestionException extends Exception{
    public InvalidQuestionException(char c)
    {
        super("Invalid Question of type " + c);
    }
}
