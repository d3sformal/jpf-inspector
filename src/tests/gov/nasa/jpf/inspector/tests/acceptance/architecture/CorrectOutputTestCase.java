package gov.nasa.jpf.inspector.tests.acceptance.architecture;

public class CorrectOutputTestCase {
  private String name;
  public String inputFile;
  public String outputFile;
  public String applicationFile;

  @Override
  public String toString() {
    return name;
  }

  public CorrectOutputTestCase(String name, String inputFile, String outputFile, String applicationFile) {
    this.name = name;
    this.inputFile = inputFile;
    this.outputFile = outputFile;
    this.applicationFile = applicationFile;
  }
}
