import java.io.*;

/*
* Alunos: Marlon Baptista de Quadros
*/

public class AsdrSample {

  private static final int BASE_TOKEN_NUM = 301;

  public static final int IDENT  = 301;
  public static final int NUM 	 = 302;
  public static final int DATA  = 303;
  public static final int PROVA	   = 304;
  public static final int MATR	 = 305;

    public static final String tokenList[] = {"IDENT",
                      											  "NUM",
                                              "DATA",
                                              "PROVA",
                                              "MATR"
                                              };

  /* referencia ao objeto Scanner gerado pelo JFLEX */
  private Yylex lexer;

  public ParserVal yylval;

  private static int laToken;
  private boolean debug;


  /* construtor da classe */
  public AsdrSample (Reader r) {
      lexer = new Yylex (r, this);
  }

  private void ARQ(){
    if(laToken == DATA){
      if(debug) System.out.println("ARQ --> data CAB DADOS");
      verifica(DATA);
      CAB();
      DADOS();
      RESTOARQ();
    }else yyerror("Esperado: data");
  }

  private void RESTOARQ(){
    if(laToken == DATA){
      if(debug) System.out.println("RESTOARQ --> data CAB DADOS RESTOARQ");
      verifica(DATA);
      CAB();
      DADOS();
      RESTOARQ();
    }else{
        if (debug) System.out.println("RESTOARQ -->       // prod. vazia");
    }
  }

  private void CAB(){
    // System.out.println(Integer.toString(laToken));
    if (laToken == PROVA){
      if(debug) System.out.println("CAB --> prova ident");
      verifica(PROVA);
      verifica(IDENT);
    }else if(laToken == IDENT){
      if(debug) System.out.println("CAB --> ident ident");
      verifica(IDENT);
      verifica(IDENT);
    }else yyerror("Esperado: prova ou ident");
  }

  private void DADOS(){
    if ( laToken == '(' ){
      if(debug) System.out.println("DADOS --> ( matr , num )");
      verifica('(');
      verifica(MATR);
      verifica(',');
      verifica(NUM);
      verifica(')');
      RESTODADOS();
    } else yyerror("Esperado: (");
  }

  private void RESTODADOS(){
    if ( laToken == '(' ){
      if(debug) System.out.println("RESTODADOS --> ( matr , num ) RESTODADOS");
      verifica('(');
      verifica(MATR);
      verifica(',');
      verifica(NUM);
      verifica(')');
      RESTODADOS();
    }else{
          if (debug) System.out.println("RESTODADOS -->       // prod. vazia");
      }
  }

  private void verifica(int expected) {
      if (laToken == expected)
         laToken = this.yylex();
      else {
         String expStr, laStr;

		expStr = ((expected < BASE_TOKEN_NUM )
                ? ""+(char)expected
			     : tokenList[expected-BASE_TOKEN_NUM]);

		laStr = ((laToken < BASE_TOKEN_NUM )
                ? new Character((char)laToken).toString()
                : tokenList[laToken-BASE_TOKEN_NUM]);

          yyerror( "esperado token : " + expStr +
                   " na entrada: " + laStr);
     }
   }

   /* metodo de acesso ao Scanner gerado pelo JFLEX */
   private int yylex() {
       int retVal = -1;
       try {
           yylval = new ParserVal(0); //zera o valor do token
           retVal = lexer.yylex(); //le a entrada do arquivo e retorna um token
       } catch (IOException e) {
           System.err.println("IO Error:" + e);
          }
       return retVal; //retorna o token para o Parser
   }

  /* metodo de manipulacao de erros de sintaxe */
  public void yyerror (String error) {
     System.err.println("Erro: " + error);
     System.err.println("Entrada rejeitada");
     System.out.println("\n\nFalhou!!!");
     System.exit(1);

  }

  public void setDebug(boolean trace) {
      debug = true;
  }


  /**
   * Runs the scanner on input files.
   *
   * This main method is the debugging routine for the scanner.
   * It prints debugging information about each returned token to
   * System.out until the end of file is reached, or an error occured.
   *
   * @param args   the command line, contains the filenames to run
   *               the scanner on.
   */
  public static void main(String[] args) {
     AsdrSample parser = null;
     try {
         if (args.length == 0)
            parser = new AsdrSample(new InputStreamReader(System.in));
         else
            parser = new  AsdrSample( new java.io.FileReader(args[0]));

          parser.setDebug(false);
          laToken = parser.yylex();

          parser.ARQ();

          if (laToken== Yylex.YYEOF)
             System.out.println("\n\nSucesso!");
          else
             System.out.println("\n\nFalhou - esperado EOF.");

        }
        catch (java.io.FileNotFoundException e) {
          System.out.println("File not found : \""+args[0]+"\"");
        }
//        catch (java.io.IOException e) {
//          System.out.println("IO error scanning file \""+args[0]+"\"");
//          System.out.println(e);
//        }
//        catch (Exception e) {
//          System.out.println("Unexpected exception:");
//          e.printStackTrace();
//      }

  }

}
