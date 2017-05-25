/**
 * User: Oleg
 * Date: 10-Feb-16
 * Time: 14:53
 */
public class PrivateFields {
    static class SecretiveParent{
        private void darkThing(){
            System.out.println("Something secret is going on");
        }
        public void doTheJob(){
            darkThing();
            System.out.println("job done");
        }
    }

    static class OpenChild extends SecretiveParent{
        public void darkThing() {
            System.out.println("Just searching for the lightswitch");
        }
    }

    static public void main(String[] args){
        new OpenChild().doTheJob();
    }
}
