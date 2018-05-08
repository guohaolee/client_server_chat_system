
public class test {
	
	 public static void hello(){
	        try{
	            System.out.println("hi");
	            return;
	            }catch(RuntimeException e){
	        }finally{
	            System.out.println("finally");

	        }

	    }

	    public static void main(String[] args){
	        hello();
	    }
	}
