package ras.exams.exams.data;

public class InvalidAnswerException extends Exception{
    public InvalidAnswerException(char c)
    {
        super("Invalid Answer of type " + c);
    }
}
