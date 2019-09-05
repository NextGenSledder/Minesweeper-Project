import java.util.*;

import tester.Tester;

class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<String> alphabet = new ArrayList<String>(Arrays.asList(
      "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
      "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
      "u", "v", "w", "x", "y", "z"));
  // The encoded alphabet: the 1-string at index 0 is the encoding of "a",
  // the 1-string at index 1 is the encoding of "b", etc.
  ArrayList<String> code;

  // A random number generator
  Random rand;

  // Create a new random instance of the encoder/decoder with a new permutation code
  PermutationCode() {
    this(new Random());
  }

  // Create a particular random instance of the encoder/decorder
  PermutationCode(Random r) {
    this.rand = r;
    this.code = this.initEncoder();
  }

  // Create a new instance of the encoder/decoder with the given code
  PermutationCode(ArrayList<String> code) {
    this.code = code;
  }

  // Initialize the encoding permutation of the characters
  ArrayList<String> initEncoder() {
    ArrayList<String> alphaCopy = new ArrayList<String>(Arrays.asList(
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
        "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y", "z"));

    int randInt = rand.nextInt(alphaCopy.size());
    ArrayList<String> newCode = new ArrayList<String>();

    while (alphaCopy.size() > 1) {
      if (!newCode.contains(alphaCopy.get(randInt))) {
        newCode.add(alphaCopy.remove(randInt));
        randInt = rand.nextInt(alphaCopy.size());
      }
    }
    newCode.add(alphaCopy.remove(0));
    return newCode;
  }

  // produce an encoded String from the given String
  // You can assume the given string consists only of lowercase characters
  String encode(String source) {
    String cipherText = "";
    int indexInString = 0;

    while (cipherText.length() < source.length()) {
      String firstChar = source.substring(indexInString, indexInString + 1);

      if (alphabet.contains(firstChar)) {
        int letterIndex = alphabet.indexOf(firstChar);
        cipherText = cipherText + code.get(letterIndex);
      }
      else {
        cipherText = cipherText + firstChar;
      }

      indexInString++;
    }

    return cipherText;
  }

  // produce a decoded String from the given String
  // You can assume the given String consists only of lowercase characters
  String decode(String code) {
    String plainText = "";
    int indexInString = 0;

    while (plainText.length() < code.length()) {
      String firstChar = code.substring(indexInString, indexInString + 1);

      if (alphabet.contains(firstChar)) {
        int letterIndex = this.code.indexOf(firstChar);
        plainText = plainText + alphabet.get(letterIndex);
      }
      else {
        plainText = plainText + firstChar;
      }

      indexInString++;
    }

    return plainText;
  }
}

class ExamplesPermutationCode {
  //ArrayList<String> alphabet = new ArrayList<String>(Arrays.asList(
  //    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
  //    "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
  //    "u", "v", "w", "x", "y", "z"));

  ArrayList<String> code1 = new ArrayList<String>(Arrays.asList(
      "x", "a", "y", "c", "f", "g", "j", "z", "l", "k", 
      "o", "m", "b", "r", "u", "e", "d", "s", "q", "w", 
      "t", "i", "p", "n", "v", "h"));    

  ArrayList<String> code2 = new ArrayList<String>(Arrays.asList(
      "s", "d", "x", "p", "o", "q", "a", "v", "h", "g", 
      "k", "b", "z", "w", "n", "u", "r", "m", "c", "j", 
      "e", "t", "l", "y", "f", "i"));

  ArrayList<String> code3 = new ArrayList<String>(Arrays.asList(
      "c", "l", "k", "o", "z", "e", "j", "f", "r", "s", 
      "n", "u", "y", "w", "d", "a", "v", "t", "g", "h",
      "q", "p", "x", "m", "b", "i"));

  PermutationCode randP = new PermutationCode();
  PermutationCode seedP1 = new PermutationCode(new Random(1));
  PermutationCode seedP2 = new PermutationCode(new Random(2));
  PermutationCode pc1 = new PermutationCode(code1);
  PermutationCode pc2 = new PermutationCode(code2);
  PermutationCode pc3 = new PermutationCode(code3);

  boolean testEncode(Tester t) {
    return t.checkExpect(pc1.encode("hi"), "zl")
        && t.checkExpect(pc1.encode("hello world!"), "zfmmu pusmc!")
        && t.checkExpect(pc1.encode("what's up?"), "pzxw'q te?")
        && t.checkExpect(pc2.encode("~xxo~"), "~yyn~")
        && t.checkExpect(pc2.encode("can you read me?"), "xsw fne mosp zo?")
        && t.checkExpect(pc3.encode("leggo my eggo"), "uzjjd yb zjjd");
  }

  boolean testDecode(Tester t) {
    return t.checkExpect(pc1.decode("zl"), "hi")
        && t.checkExpect(pc1.decode("zfmmu pusmc!"), "hello world!")
        && t.checkExpect(pc1.decode("pzxw'q te?"), "what's up?")
        && t.checkExpect(pc2.decode("~yyn~"), "~xxo~")
        && t.checkExpect(pc2.decode("xsw fne mosp zo?"), "can you read me?")
        && t.checkExpect(pc3.decode("uzjjd yb zjjd"), "leggo my eggo");
  }

  boolean testInitEncoder(Tester t) {
    return t.checkExpect(seedP1.initEncoder(), code2)
        && t.checkExpect(seedP2.initEncoder(), code3);
  }
}