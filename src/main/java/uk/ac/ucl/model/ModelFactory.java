package uk.ac.ucl.model;

import java.io.File;
import java.io.IOException;

// This class gives access to the model to any other class that needs it.
// Calling the static method getModel (i.e., ModelFactory.getModel()) returns
// an initialised Model object. This version limits the program to one model object,
// which is returned whenever getModel is called.
// The factory also illustrates how a data file can be passed to the model.

public class ModelFactory
{
  private static Model model;
  public static Model getModel(String fileName){
    if (model == null)
    {
      model = new Model();
      // Note where the data file is stored in the data directory,
      // and the pathname to locate it.
      // The data should be read the file once, not every time the model is accessed!
      model.readFile(fileName);
    }
    return model;
  }
  public static Model getModel() throws IOException
  {
    // This is the default one that will be opened, which can then be opened or other files can be opened over it
    return getModel("data/patients100.csv");
  }
}
