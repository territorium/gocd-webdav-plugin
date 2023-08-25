import cd.go.artifact.util.FileMapper;

public class Main {

  public static void main(String[] args) {

    for (FileMapper mapper : FileMapper.list("build/%.aab", "/tmp")) {
      String path = mapper.remap("release");
      System.out.println(path);
    }
  }

}
