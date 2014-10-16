---
---

This example shows how to serialize objects to JSON and back. For simplicity we will serialize to a std::stringstream, but we could write to any generic data sink - a class having a write(void*, int) method. Any c++ std::istream or std::ostream will work, but if you want exceptions on the IO layer instead of the parsing layer, you should use MGen's own streams (which can wrap std::istreams and std::ostreams).

In this example we use the following includes and namespace directives, and create a global class registry for our following functions to use:

{% highlight c++ %}

#include <com/fruitcompany/ClassRegistry.h>
#include <mgen/serialization/JsonPrettyWriter.h>
#include <mgen/serialization/JsonReader.h>

using namespace mgen;
using namespace com::fruitcompany;
using namespace com::fruitcompany::fruits;

// A class registry for type identification
const ClassRegistry registry;

{% endhighlight %}

Then we define our serialization functions:

{% highlight c++ %}

std::string toJSON(const MGenBase& object) {

  // Create a target to stream the object to
  std::stringstream stream;

  // Create a writer object
  JsonPrettyWriter<std::stringstream, ClassRegistry> writer(stream, registry);

  // Write the object
  writer.writeObject(object);

  // Return the written string
  return stream.str();
}

template <typename T>
T fromJSON(const std::string& json) {

  // Create a data source to stream objects from
  std::stringstream stream(json);

  // Create a reader object
  JsonReader<std::stringstream, ClassRegistry> reader(stream, registry);

  // Read object. You can read T* polymorphicly with reader.readObject<T>()
  return reader.readStatic<T>();
}

{% endhighlight %}

Lastly comes the main function which uses the above:

{% highlight c++ %}

int main() {

  // Create some objects
  const Apple apple(Brand_A, 4);
  const Banana banana = Banana().setLength(5).setBrand(Brand_B);

  // Serialize them to JSON and print them
  std::cout << toJSON(banana) << std::endl;
  std::cout << toJSON(apple) << std::endl;

  // Read the objects back from their serialized form
  const Apple appleBack = fromJSON<Apple>(toJSON(apple));
  const Banana bananaBack = fromJSON<Banana>(toJSON(banana));

  // Check that they are still the same
  std::cout << (apple == appleBack) << std::endl;
  std::cout << (banana == bananaBack) << std::endl;

  return 0;
}

{% endhighlight %}