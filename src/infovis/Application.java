package infovis;


/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/10/12
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class Application {

    private static Controller ctr = Controller.getInstance();

    public static void main(String[] args)
    {
        ctr.init();
    }

}
