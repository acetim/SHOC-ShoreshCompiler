plugins {
	java
	id("org.graalvm.buildtools.native") version "0.11.5"
}

graalvmNative{
	binaries{
		named("main"){
			mainClass.set("Main")
			imageName.set("shoc")
			buildArgs.add("--no-fallback")
		}
	}
}
tasks.named<Jar>("jar"){
	manifest{
		attributes["Main-Class"]="Main"
	}
}


group = "com.shoc"
version = "1.0.2"

repositories {
	mavenCentral()
}
dependencies {

}
