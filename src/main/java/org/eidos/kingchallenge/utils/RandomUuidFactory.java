package org.eidos.kingchallenge.utils;





public class RandomUuidFactory 
{
    private static final RandomUuidFactory _SINGLETON = new RandomUuidFactory();
    
	public static enum Mode {
	    ALPHA, ALPHANUMERIC, NUMERIC 
	}

    /**
     * 
     * The default constructor is explicit so we can make it private and 
     * require use of getInstance() for instantiation.
     * 
     * @see #getInstance()
     *
     */
    private RandomUuidFactory()
    {
    	
    }
    
      public String createUUID() throws Exception{
    	  return generateRandomString(20,Mode.ALPHA);
    }
      public static String generateRandomString(int length, Mode mode) throws Exception {

  		StringBuffer buffer = new StringBuffer();
  		String characters = "";

  		switch(mode){
  		
  		case ALPHA:
  			characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  			break;
  		
  		case ALPHANUMERIC:
  			characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
  			break;
  	
  		case NUMERIC:
  			characters = "1234567890";
  		    break;
  		}
  		
  		int charactersLength = characters.length();

  		for (int i = 0; i < length; i++) {
  			double index = Math.random() * charactersLength;
  			buffer.append(characters.charAt((int) index));
  		}
  		return buffer.toString();
  	}
    /**
     * 
     * @return The singleton instance of this class.
     *
     */
    public static RandomUuidFactory getInstance()
    {
        return _SINGLETON;
    }
}