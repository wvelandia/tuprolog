

## Introduction ##
This document explains the structure of the repository for the tuProlog project. This document will explain also the policies used to update the contents in the repository in case of bug fixes, releases, feature implementations and so on. The document is intended for the people that have to manage the repository, for the developers it is not required to read it.

This document is targeted on SVN (Subeversion) source code version system therefore sometimes in the examples we will show specific commands for this system. Since the system used here is subversion we choose to follow the main repository layout convention (described in the [SVN book](http://svnbook.red-bean.com/)) in order to ensure that other users that are familiar with subversion or with its conventions can easily navigate and use our repository, however we add some specific strategies that are not exactly described in the SVN book. Some of the concepts are also taken from some articles of [Ariejan de Vroom](http://ariejan.net/).

This document we will give as known some basic concepts about version control systems and subversion, like: repository, working copy, revision, branch, tag, merge, commit, update, switch, check out, copy-modify-merge model. For more informations on this concepts we advice to read the [SVN book](http://svnbook.red-bean.com/), in the Appendix A a brief description of a typical work cycle is given.

## Structure of the repository ##
As mentioned in the introduction, following the SVN convention, our repository will contain a "trunk" subdirectory for the main development line, a "branches" subdirectory in which specific branches for bug fixing and thesis will be created, and a "tags" subdirectory in which specific tags will be created. The tuProlog project is composed of four parts: the java version of the engine, the eclipse plugin, the android application both based on the java version and the .NET version of the engine automatically produced from the java version using the tool [IKVM.NET](http://www.ikvm.net/). These four parts are divided into four different projects, all hosted in the same repository in order to simplify the relationships between them as described in the following sections.

```
/svn
	/2p
		/trunk
		/branches
		/tags
	/2p-android
		/trunk
		/branches
		/tags
	/2p-plugin
		/trunk
		/branches
		/tags
	/2p-net
		/trunk
		/branches
		/tags
```

## Trunk Management ##
The trunk contains the most current development code at all times. This is where we work up to our next major release of code. The trunk is designed as a space where the application is always in a consistent state (**successful build and test**), in other words every commit to the trunk should not break the application. In accordance to the previous statement before commit to the trunk the application should be tested in deep in order to ensure that anything is changed, for this reason the development directly against the trunk is allowed only in the cases where the changes are very small and easy to describe and understand. Otherwise if the modification is deep (and maybe require a lot of changes before reaching the final stable result) **it is mandatory** to create another branch (they are intended exactly for this purpose) and develop the modification against it, at the end of the process, when a stable result is reached, the work done in the branch can be merged inside the trunk. Using this approach the development cycle will not break because when someone is working on a bug fix in its specific branch another developer, at the same time, can develop a new feature and at the end, the work of the two can be merged together in order to have a better application with less bugs.

In summary:
  1. The application in the trunk must be in a consistent state (build + tests) in any moment;
  1. Every commit to the trunk should bring the application in a consistent state;
  1. If the point 2 is impossible you must create a branch.

### Peculiarity of the Eclipse Plugin Trunk ###
In order to take advantage of the installation and automatic updates capabilities that the Eclipse platform offers for its plugins, it is necessary to configure a web server, called Update Site, to serve the appropriate jar files needed to install or update a plugin. Since the simplicity in the management is one of our goal we decided to use the repository itself as an Updated Site instead of configuring an ad hoc server. In particular, the [trunk](http://tuprolog.googlecode.com/svn/2p-plugin/trunk/) of the Eclipse plugin project contains the folder "tuPrologUpdateSite" that contains all the files needed to Eclipse to recognize the different versions of the plugin and to install them.

With this setup, to release a new version of the plugin it is simply necessary to build it following this [guide](http://code.google.com/p/tuprolog/wiki/GuidaPubblicazioneNuoveVersioniPluginEclipse) and commit to the trunk the new jar files that have been generated. After the commit the new version is immediately available and Eclipse should recognize it as an update in the case the plugin was already installed.

## Branches management ##
As mentioned in the previous section the branches are the tools to not break the development cycle. A branch, in short, is a copy of the trunk in a specific moment. After the creation of the branch it has its own life, so, in other words, each commit into a branch is not committed into the trunk. This way you are free to commit bit by bit changes into the branch without break the stable version of the application in the trunk. This approach will give us also more control over the changes that each developer made, in fact we can analyze each small modification and so we can easily recognize a new bug that can be introduced during the development against a branch. When the work on the branch is finished the changes made in it should be reintegrated inside the trunk in order to ensure that the trunk contains the latest version of the application.

Before describing how to manage the different kind of branches used in our structure, we will focus on two important topics regarding branching and merging: synchronization and reintegration of a branch.

### Keeping a Branch in Sync ###
As explained before, the usefulness of the branches is that your are “disconnected” from the trunk so you let the other developers work without any problems. This is of course a nice feature but lets consider this scenario: while you are working on your branch the code in the trunk is changed a lot, very deeply, when you have to reintegrate your work into the trunk you will recognize that your new feature is not working anymore in the new version of the application or you have a lot of conflicts to resolve. This is a typical problem that you will encounter if you work on your own branch in total isolation. The best way to handle this is to replicate the changes made in the trunk to your own branch, just to make sure they mesh well with your changes. Subversion is aware of the history of your branch and knows when it is divided away from the trunk. To replicate the latest trunk changes to your branch, first make sure your working copy of the branch is “clean”—that it has no local modifications reported by svn status. Then simply run:

```
$ svn merge ^/2p/trunk 
--- Merging r345 through r356 into '.': 
      U Theory.java 
      U Struct.java $
```

This basic syntax—`svn <merge> <URL>`—tells Subversion to merge all recent changes from the URL to the current working directory (which is typically the root of your working copy). Also notice that we're using the caret (^) syntax to avoid having to type out the entire /trunk URL.
**After running the prior example, your branch working copy now contains new local modifications, and these edits are duplications of all of the changes that have happened on the trunk since you first created your branch**. After performing the merge, you might also need to resolve some conflicts or possibly make some small edits to get things working properly. If you encounter serious problems, you can always abort the local changes by running "svn revert . -R" which will undo all local modifications. If things look good, however, you can submit these changes into the repository:

```
$ svn commit -m 
"Merged latest trunk changes to TH-Montanari.”.
```

[Here](https://code.google.com/p/tuprolog/wiki/HOWTOBranchSynchronization) it is possible to find a guide that explains how to synchronize a branch using the Subclipse 1.8 plugin for Eclipse and [here](http://code.google.com/p/tuprolog/wiki/HOWTOBranchSynchronization_Tortoise) there is the same guide for TortoiseSVN.

Given the past experience with the tuProlog project we think that it is not mandatory to impose a fixed period for branch synchronization (like once a week or something similar). However, there are situations where a branch synchronization is needed to simplify the successive reintegration of the branch. For example when there is overlapping between the work of two or more developers and one of the branches is reintegrated into te trunk: in that case it is necessary to synchronize the other branches. Another example is when there is a long-lived branch: in this case in fact it is possible that many other branches have been reintegrated in the trunk and if the long-lived branch is not synchronized often, its successive merge with the trunk could be difficult because they are too far apart. It is responsibility of who manages the repository to detect these situations and request a branch synchronization to the developers that are working on active branches.

### Reintegrating a Branch ###
This is the operation that has to be done when the work on a branch is finished and it is necessary to make the changes available to the others developers, merging the branch with the trunk. The process is simple. First, bring the branch in sync with the trunk again, just as you've been doing all along. Now, you use svn merge with the --reintegrate option to replicate your branch changes back into the trunk. **You will need a working copy of the trunk to do that**. Your trunk working copy cannot have any local edits or be at mixed-revisions. While these are typically best practices for merging, they are required when using the --reintegrate option.
Once you have a clean working copy of the trunk, you are ready to merge the branch back into it:

```
$ svn update # (make sure the working copy is up to date)
$ svn merge --reintegrate ^/2p/branches/TH-Montanari
--- Merging differences between repository URLs into '.': 
       U Theory.java 
       U Struct.java 
       U ant.xml.
$ # build, test, verify, ...
$ svn commit -m "Merge TH-Montanari back into trunk!" 
         Sending	Theory.java 
	 ...
	Committed revision 391.
```

Before committing, it is necessary to test the new version of the trunk and compare the results with the previous ones. For the Java version of tuProlog are available some Ant targets that can be used to automatize the test process, for the other projects these targets are not available yet so the test has to be done manually.

The branch has now been remerged back into the main line of development. By specifying the --reintegrate option, you are asking Subversion to carefully replicate only those changes unique to your branch. (And in fact, it does this by comparing the latest trunk tree with the latest branch tree: the resulting difference is exactly the branch changes).
Now that the branch is merged into the trunk, you may wish to remove it from the repository:

```
$ svn delete ^/2p/branches/TH-Montanari -m "Remove TH-Montanari, reintegrated with trunk in r391."
Committed revision 392.
```

**This operation is not totally destructive, indeed even though the branch is no longer visible in the /branches directory, its existence is still an immutable part of the repository's history**. A simple svn log command on the /branches URL will show the entire history of the branch. The branch can even be resurrected at some point (see the section called “Resurrecting Deleted Items” in the SVN book).

In summary, to reintegrate a branch:
  1. make the last synchronization with the trunk;
  1. get a working copy of the trunk and update it;
  1. merge the branch with the working copy;
  1. test the code;
  1. commit the changes;
  1. delete the branch.

[Here](https://code.google.com/p/tuprolog/wiki/HOWTOBranchReintegration) it is possible to find a guide that explains how to reintegrate a branch using the Subclispe 1.8 plugin for Eclipse.

The following sections describe the three categories of branches used during the work on tuProlog: bug fix, experimental and thesis. These conventions have been developed in order to understand immediately from the name of the branch which is its purpose.

### Bug Fix Branches ###
When a bug is recognized, if that bug is easy to solve and can be solved with just **one commit**, it is possible to fix it directly from where it was discovered (trunk or branch) but if the bug is more complex and requires more time and work to be fixed, the better way is to create a new branch and develop the solution on that branch. For this kind of branches we chose the following template for the name of the relative directory "BUG-[ID](bug.md)", where bug ID  is the Identification Number of the bug in the bug tracker or a name that recalls the bug itself. An example:

```
/2p
	/trunk
	/branches
		/BUG-3456
	/tags
```

The bug branch can be used like any other branch and when the work is finished it has to be reintegrated into the trunk or into the branch as described in the previous sections

We recall here the best procedure to handle a bug:
  1. after the discovery insert a detailed description in the bug traker;
  1. create one or more tests that expose the bug;
  1. create a bug branch to fix it;
  1. reintegrate the branch when the work is finished.

### Experimental Branches ###
The experimental branches are intended to be used when there is the necessity to try a new technology or when a student works on a specific project related to tuProlog (called "Attività Progettuale" in italian). Since this could be a dangerous operation and we do not want to compromise the work in the trunk, a new branch is the right solution. In this case the template is the following: "TRY-[of the exepriment](keywords.md)", where with some keywords you have to describe what is going to change, for example:

```
/2p
	/trunk
	/branches
		/BUG-3456
		/TRY-JDK1.7
		/TRY-generics
	/tags
```

An experimental branch can be abandoned (or deleted) or reintegrated into the trunk

### Thesis Branches ###
A typology of branch is provided for work done on tuProlog by students during their bachelor or master thesis. Usually a thesis on tuProlog is like trying a new technology or a new approach to solve a problem or to improve the quality of the engine so this kind of branch is like an experimental branch but a new template for the name is used in order to let the professors find the right directory immediately. The template is: "TH-[of the student](surname.md)". An example:

```
/2p
	/trunk
	/branches
		/BUG-3456
		/TRY-JDK1.7
		/TRY-generics
		/TH-Montanari
	/tags
```

To enable the student as a committer on the tuProlog repository, the student must have a google account and he has to be added in the [People section](https://code.google.com/p/tuprolog/people/list) giving him the "Committer" role. The "Duties" filed should contain a short description of the thesis and the URL of the branch associated to the student, in order to let the people involved in the project to better understand in which direction the work is going on.

When the work is finished the branch will be reintegrated into the trunk if it is good enough otherwise the branch can be deleted or maintained if future work will take place.

## Tags Management ##
Exactly as a branch, a tag is a copy of the trunk (or of a branch) in a specific moment, the only difference is that we decided (SVN convention) to never commit to a tag. This way it will remain a snapshot of our project in a certain moment forever. This seems to be redundant since in a version control repository each change is tracked (revisions), this is true, but the reason why we use tags instead of revisions to track releases is that it's not so easy to remember that release 1.0 of a piece of software is a particular subdirectory of [revision 4822](https://code.google.com/p/tuprolog/source/detail?r=4822), coping the state of the software in a certain directory makes it a trivial task. So the gold rule for the tags is: **never commit to one of them**.

### Release Tags ###
These kind of tags mark the release (and state) of our code at that the release point. The template for the name of the relative directory is: "REL-[`version number`]", where the version number is defined by the rules described [Here](https://code.google.com/p/tuprolog/wiki/ProjectsVersionManagement). Here is reported an example:

```
/2p
	/trunk
	/branches
	/tags
		/REL-2.3.0
```

During the tag operation performed using the Ant script discussed below, the folder `^`/2p/tags/REL-x.y.z/release, for the Java and .NET project, is automatically populated with the zip file containing the actual release. This file represents the release that has to be distributed to the users therefore, if needed, it can be referenced from any platform used for the distribution.


## Relationships between projects ##
tuProlog is a characteristic project consisting of four sub-projects: the Java SE version, the Android version, the Eclipse plugin and the .NET version. The Java version is the actual implementation of the Prolog engine (in addition to the user interface for Java SE), while the Android version and the Eclipse plugin are simply the implementations of the user interfaces for the respective platforms. The Android version and the Eclipse plugin use the core of the engine, written in java, as a library and build around it the user interface: an Android app and a plugin respectively. For the .NET version the situation is slightly different. In fact the .NET version is obtained automatically converting the java bytecode into .NET bytecode using the tool [IKVM.NET](http://www.ikvm.net/): the result of the conversion is a working executable file. The only code implemented specifically for the .NET platform is the OOLibrary that is the counterpart of the JavaLibrary on the Java platform (please refer to the manual for more informations on these libraries).

In more detail, the Android version and the Eclipse plugin have as dependency the file tuprolog.jar, produced by the Java version. This file contains only the core of the tuProlog engine (the Java SE UI is contained in the file 2p.jar). The files 2p.jar and tuprolog.jar are used as input to the IKVM.NET tool to produce the files 2p.exe and tuprolog.dll. After the conversion with IKVM.NET the files 2p.exe and tuprolog.dll are already the .NET version, the only code that is added (in C#) is the implementation of the OOLibrary. The image below should clarify the situation.

<img src='http://tuprolog.googlecode.com/svn/wiki/images/NotesOnTheManagementOfTheRepository/tuPrologVersions.png' height='50%' width='50%' />

### Enforce Dependencies using svn:externals ###
As described in the previous section, three projects out of four depends on two files, 2p.jar and tuprolog.jar. This dependency as to be reflected on the repository as well in order to ensure that the developers have everything they need to start working immediately after the checkout, without the need to download additional software using other checkout commands and set-up references. Subversion offers a mechanism called **Externals Definitions** that can be used to achieve this. An externals definition is a link to a resource on the repository (or on another repository) that can be attached to any versioned directory using the `svn:externals` property. When the folder that contains the property is checked out, Subversion will read the property and checkout also the resources referenced by it. This way, to build a working copy that consists of many resources in different locations on the repository, it is not necessary to do several checkout manually but, once the externals definitions are in place, everything happens automatically upon a single checkout operation.

During the design of the repository structure we had in mind two goals: simplicity and immediacy. We tried to design the repository in a way that the projects, after the checkout, could be used immediately, limiting the operations from the developers as much as possible. Therefore **we decided to put the required files (2p.jar and tuprolog.jar) directly on the repository and use externals definitions to link them to the projects that require them**. This way, when a developer checks out the Android project for example, the file tuprolog.jar will be automatically downloaded and copied in the "libs" folder, so the developer is immediately ready to start his work. This is slightly against the "SVN best practices" that discourage to put binaries on the repository because it should be used only for code. In these situations the SVN book advices to setup externals definitions so as to download the code necessary to compile the other project. This means, for example, that if a developer has to work on the Android version an externals definition is needed to download the entire Java version, otherwise the Android app will not compile. This complicates the use of the project because the developer has to deal with may files that are not directly related to his project and it is necessary that also the code downloaded (the Java version in the example above) compiles, so this requires to download also its dependencies and setup the IDE to correctly compile it (classpath, etc.).

After the decision to put the binaries on the repository there was another question to answer, where can we put those files? The fundamental idea is that the projects that depend from the tuProlog core are released immediately after the release of a new tuProlog version. In fact in that moment the new tuprolog.jar file is available and the other projects can be built in order to incorporate the new core. Therefore there is a "virtual link" between each project and a specific version of the core, something like: the current version of the Android app is "linked" to tuProlog version 2.8 because it uses its core (this is also reflected in the scheme that we adopted for the [version numbering](http://code.google.com/p/tuprolog/wiki/ProjectsVersionManagement)). In order to keep this link as visible as possible, to eventually simplify the analysis of older versions of the project, we decided to put the binary files (2p.jar and tuprolog.jar) in the tags folder. Moreover, the tags folder is by definition "untouchable" so it represents a good place where to put binary files that must not be modified. **When a version of the core is tagged with x.y.z as version number, the two files are generated and copied in the folder /2p/tags/REL-x.y.z/build/archives/** (look at the following sections for more information on the automatization of this process using Ant). At this point, in the projects that requires tuprolog.jar (Android and Eclipse) is used an externals definition on the root folder of the project in the trunk in oder to checkout the required file in the "lib" folder of the project ("libs" folder for Android).

Example:
  * in the folder /2p/tags/REL-2.8.0/build/archives/ there are the files, 2p.jar and tuprolog.jar;
  * in the root folder of the Android project in the trunk (/2p-android/trunk) is used the following "svn:externals" property
```
^/2p/tags/REL-2.8.0/build/archives/tuprolog.jar libs/tuprolog.jar
```
This property says that the file `^`/2p/tags/REL-2.8.0/build/archives/tuprolog.jar (note the use of the `^` character in replacement of the entire URL of the repository) has to be copied in the folder "libs" upon checkout. This way, when the developer checks out the Android project, subversion will also automatically download tuprolog.jar inside "libs". Moreover, this use of the property makes immediately recognizable to which version of the core the current Android app is linked.

In a similar way externals definitions are used to copy the tuProlog manual into the working copy ("doc" folder), ensuring in this way that the developer obtain everything he needs with a single checkout.

To summarize:
  * the files required by the projects are placed in binary format in the repository;
  * the binaries are placed in the tags folder;
  * using `svn:externals` the binaries are copied automatically where required;
  * the `svn:externals` properties are always attached to the "trunk" folder of the project.

The last point has been chosen asa convention to simplify the management of the svn properties. This way in fact, it is well known where all the `svn:externals` are and there is no need to search for them in other folders. When a branch or tag is created, those properties are placed in the root folder, so in the folder "TH-Montanari" or "REL-2.8.0".

#### .NET Special Considerations ####
The situation for the . NET version of tuProlog is slightly different than the Android and Eclipse version. In fact, in .NET the file tuprolog.jar is not used as a library, as in Android and Eclipse, but it is used to generate the actual .NET version simply converting the java bytecode to .NET bytecode using IKVM.NET. This means that there is nothing on the .NET side that uses tuprolog.jar, instead the OOLibrary and the examples on the .NET side uses tuprolog.dll and 2p.exe. At this point the question was, if it would be better to link the files tuprolog.jar and 2p.jar inside the .NET projects and in this case the developer would have to produce tuprolog.dll and 2p.exe by himself, or produce automatically those files when a new tuProlog version for Java is tagged and link those files inside the .NET projects, in this way the developer, upon a single checkout, obtains everything he needs to work. We chose the second alternative because it was the best choice to keep the complexity low. In fact, probably some developers will work only on the OOLibrary or on future libraries and they do not need any knowledge about how the .NET version is created and about IKVM.NET, therefore generating the exe and dll files in advance permits to let the the developers focus on what they have to do, without bothering about the generation of these files.

In more details, when a new tag for the Java version is created, using the Ant script discussed below, IKVM.NET is used to generate some .NET files, in particular:
  * 2p.exe, tuprolog.dll and javassist.dll generated from 2p.jar, tuprolog.jar and javassist.jar respectively. They are placed in the folder `^`/2p/tags/REL-x.y.z/build/archives/net/.
  * fit.dll and junit.exe generated from fit.jar and junit.jar. They are placed in `^`/2p/tags/REL-x.y.z/build/archives/net/lib/.
  * 2pWithUnitTests.dll and fixtures.dll. They are required to run the Java tests on the .NET platform (refer to Montanari's thesis) and are placed in `^`/2p/tags/REL-x.y.z/build/archives/net/test/.

For example, the tag of the Java version 2.8.0 looks like this:
```
/2p
  /tags
      /REL-2.8.0
          /build
              /archives
                  /2p.jar
                  /tuprolog.jar
                  /net
                      /2p.exe
                      /tuprolog.dll
                      /javassist.dll
                      /lib
                          /fit.dll
                          /junit.exe
                      /test
                          /2pWithUnitTests.dll
                          /fixtures.dll  
```

At this point, similarly to what is already done for Android and Eclipse, those files are linked to the "lib" folder in the .NET trunk (`^`/2p-net/trunk/lib) using externals definitions, so the projects on the .NET side (OOLibrary and examples) can reference them and the developer does not need to generate them manually.

This solution is a bit "dirty" because in the tags for the Java version are placed .NET files, but in this way we are more uniform with the management of the dependencies for the other projects (Android and Eclipse) and simplify the work of the .NET developers.

In addition to the dependencies analyzed before, the .NET projects depend also on the IKVM.NET libraries. In this case we adopted a similar approach: the IKVM.NET libraries are copied in the "ikvm" folder (`^`/2p-net/ikvm) keeping the version number:
```
/2p-net
  /ikvm
    /ikvm-7.0.4335.0
    /ikvm-7.1.4532.2
    /ikvm-7.2.4630.5
```

Using externals definitions the required libraries are linked in the "lib" folder in the trunk (`^`/2p-net/trunk/lib) and referenced by the .NET projects. In this way we avoid coping the IKVM.NET libraries directly into the projects saving in this way space on the repository because the required libraries are a discrete number. Moreover, looking at the `snv:externals` property, it is always possible to understand which version of IKVM.NET is being used.

### Impact on the Repository Management ###
The use of externals definitions has several implications on the repository management. We would like to discuss these implications distinguishing three moments:
  * release of a new tuProlog core;
  * release of a new IKVM.NET version;
  * release of a new tuProlog manual version.

In the first case, when a new tuProlog core is released it is necessary to test it also on the other platforms and release them as well. To do this it is necessary to create the Java tag using the Ant script and then change the values of the `svn:externals` in the other projects to link the new files. For example if the value of the property for the Android project is:
```
^/2p/tags/REL-2.8.0/build/archives/tuprolog.jar libs/tuprolog.jar
```
After the the tag of the release 2.9.0 it should be changed to:
```
^/2p/tags/REL-2.9.0/build/archives/tuprolog.jar libs/tuprolog.jar
```
After the commit of this change it is necessary to do an UPDATE of the working copy, in this way Subversion replaces the old tuprolog.jar with the new one. If the application works correctly even with the new core it can be tagged and released. In the rare case that a new tuProlog core is released while some branches are still active, the more conservative choice is to modify the externals definitions also on the branches and resolve the problems that could arise from this change directly in the branch. If the externals definition is not changed in the branch, probably an svn conflict regarding the value of the property will arise when the branch is reintegrated in the trunk and the problems due to the new version of the core could be more difficult to solve, because maybe the developer that was working on the branch is not available anymore.

Similarly, when a new version of IKVM.NET is released it is necessary to copy the libraries in the "ikvm" folder (`^`/2p-net/ikvm) and change the value of the `svn:externals` property on the .NET trunk. For example from this value:
```
^/2p-net/ikvm/ikvm-7.2.4630.5/bin/IKVM.OpenJDK.Core.dll IKVM.OpenJDK.Core.dll
...
```
To this value:
```
^/2p-net/ikvm/ikvm-7.3.0.0/bin/IKVM.OpenJDK.Core.dll IKVM.OpenJDK.Core.dll
...
```
Even in this case it is necessary to do an UPDATE of the working copy to replace the old files.

Exactly as in the two cases above, when a new version of the tuProlog manual is available it is necessary to change the value of the svn properties to download it from the new location.

**We remember that the `svn:externals` properties are always attached to the trunk folder of each project that requires it**.

## Management Automation using Ant ##
[Apache Ant](http://ant.apache.org/) is extensively used in tuProlog to automate tasks that regards both the development of the projects, as explained [here](http://code.google.com/p/tuprolog/wiki/GuideOnTheDevelopmentOfTheProjects), and the management of the repository. This section will focus only on the Ant targets that must be used to manage the repository. In particular, all tuProlog projects have an Ant target that must be used to tag the code. This target must be used because it performs three important tasks: it creates all the files that are necessary to the other projects (2p.jar, tuprolog.jar, 2p.exe, etc.), it copies the release zip into the tag folder for Java and .NET and it changes the name of the projects in the file ".project" so when different tags are imported into Eclipse there are no name conflicts.

For everything to work properly, it is crucial to understand that Ant must be properly configured in Eclipse: this is NOT automatic - in fact, in most cases the default Ant configuration will NOT work in our case.

So, before trying to run any target, please check the Eclipse preferences in Window->Preferences->Ant->Runtime->Classpath->Global Entries, and see whether an external JAR has been set for "tools.jar". IF NOT, add your path to the lib/tools.jar (in the JDK subfolder) so that it points to the proper JDK.
If you do not do so, Ant will use a default JRE, independently of the
system path and of the path set in environment.properties. This would
tipically result in the Javac compiler not being found -> build failure.

### Java ###
The Java version of tuProlog contains many Ant targets to build the engine, to test it, etc. but only one must be used to manage the repository. ~~This target is called "12.tag" and, as the name suggests it tags the current working copy on the repository. In more detail the target performs the following operations:~~
  1. ~~it builds the files tuprolog.jar and 2p.jar that have to be copied in the tag because are used by the other projects;~~
  1. ~~it builds the files tuprolog.dll, 2p.exe and the other .NET files that are used by the .NET projects;~~
  1. ~~it builds the release zip file that has to be copied in the "release" folder inside the tag;~~
  1. ~~it finally tags the code and during the tag it performs these other operations:~~
    1. ~~it changes the name of the Eclipse project in the ".project" file. This renaming is done in order to avoid name conflicts when many tags are imported in Eclipse.~~
    1. ~~it imports all the files created in the previous steps inside the correct folders.~~


~~Since this target has to build also the .NET files it must to be executed on a Windows machine where it is available a copy of the IKVM.NET binaries and the .NET framework. This is necessary because the IKVM.NET tool that converts java bytecode into .NET bytecode is written on the .NET platform and therefore requires its presence to run. The IKVM.NET tool is supported also on Mono for Linux and OS X but the current version of the Ant script is not able to run correctly on these platforms. In order to work correctly the target requires two properties that are defined in the file ant/environment.properties: the path to the IKVM.NET folder in the local filesystem and the Google Code username that has to be used to tag the code.~~

The steps to be performed to tag the Java version are the following:
  1. verify that Ant is properly configured in Eclipse: to this end
> > check Windows->Preferences->Ant->Runtime->Classpath->Global Entries, and
> > add the path to the lib/tools.jar (in the JDK subfolder).
> > If you do not do so, Ant will use a default JRE, independently of the
> > system path and of the path set in environment.properties. This would
> > tipically result in the Javac compiler not being found -> build failure
  1. modify the version number in the file [VersionInfo.java](http://code.google.com/p/tuprolog/source/browse/2p/trunk/src/alice/util/VersionInfo.java) as specified [here](http://code.google.com/p/tuprolog/wiki/ProjectsVersionManagement);
  1. in the file ant/environment.properties setup the path to the IKVM.NET folder and the Google Code username.
  1. ~~run the target "12.tag", it will request the password for the specified user and the password can be found at this address https://code.google.com/hosting/settings.~~
  1. run the target "11.release.bin" to produce all the release files (2p.jar, tuprolog.jar, 2p.exe, tuprolog.dll, REL-x.y.z.zip, ...);
  1. commit all the changes (basically you will commit the files produced at the previous step);
  1. tag the code using your SVN client (e.g. Sublicpse, TortoiseSVN, ...). Remember to use the correct name for tag folder: "REL-x.y.z".~~

### Android ###
The Android release must be performed by an Android-enabled Eclipse installation, such as the ADT tool.

Like the Java version, the Android version also has an Ant target to tag the code. The target is called "03.tag" and apart from tagging the code it only changes the name of the project in ".project" file.

The steps to be performed to tag the Android version are the following:
  1. modify the value of the `svn:externals` property to refer to the new version of the core, as explained before (or below in the .NET case);
  1. commit, update and verify that everything is OK (as in the .NET case);
  1. modify the version number in the file [AndroidManifest.xml](http://code.google.com/p/tuprolog/source/browse/2p-android/trunk/AndroidManifest.xml) as specified [here](http://code.google.com/p/tuprolog/wiki/ProjectsVersionManagement);
  1. run the application in the emulator to force the apk file to be generated;
  1. in the file ant/environment.properties setup the Google Code username;
  1. run the target "03.tag": this will request the password for the specified Google Code, which can be found at https://code.google.com/hosting/settings.

### Eclipse ###
Exactly as for the Android version, the Eclipse version has an Ant target called "03.tag" that tags the code and changes the name of the project in ".project". It requires to set up the Google Code username in the file ant/environment.properties.

The steps to be performed to tag the Eclipse version are the following:
  1. modify the value of the `svn:externals` property to refer to the new version of the core as explained before;
  1. commit, update and verify that everything works as expected;
  1. follow [this guide](http://code.google.com/p/tuprolog/wiki/GuidaPubblicazioneNuoveVersioniPluginEclipse) to publish the new version of the plugin;
  1. in the file ant/environment.properties setup the Google Code username.
  1. run the target "03.tag", it will request the password for the specified user and the password can be found at this address https://code.google.com/hosting/settings.

### .NET ###
In the .NET version, there is obviously no Eclipse project. Several Visual Studio projects are present, but Visual Studio is not needed in order to tag the code: only the Ant tool and the "csc" compiler are needed. Ant could be run from the command line, and the "csc" compiler must be on the system path.
The IKVM tool must also be available in the path, since the ant script has to copy the required DLL libraries in the release (it is not used to compile, however -- this step has already been performed during the generation of the main Java target).

First, go to the 2p-net\trunk\ant subdirectory in the repository.

So, in order to tag the .NET version:
  1. first, modify the externals settings and svn properties (details below)
  1. then, launch Ant from a terminal window with the "02.tag" target. This will compile all the required Csharp files, via csc, and tag the release as appropriate.
Please note: the Ant tool should be run from the "ant" folder, running the command "ant 02.tag". The tool will compile and tag everything, that is, it will also copy the release zip file to the tag folder.

If you do not have a stand-alone Ant installation, there is one in a subfolder of the Eclipse folder - just look for the proper path.

If you use a newer IKVM version, load it to the repository, too: you will find
a "2p-net/ikvm" folder, with subfolders for each ikvm version. Then, remember to change all the externals in the trunk (see the ".NET Special Considerations" section).

NB: warnings are possible during the build, about IKVM: most of them can be safely ignored, they just refer to unsed DLLs

The steps to be performed to tag the .NET version are the following:
  1. modify the value of the `svn:externals` property to refer to the new version of the core (2p.exe, tuprolog.dll, etc.): these properties can be seen in the "2p-net\trunk" folder, via Tortoise or Versions' Inspector. Then:
  1. commit and update: this is necessary to force SVN to reload the new dependent files in the update phase;
  1. in the ant/environment.properties file, set the path to the IKVM.NET folder and the Google Code username.
  1. run the target "02.tag", it will request the password for the specified user and the password can be found at this address https://code.google.com/hosting/settings.

Please remember: the "lib" folder is always empty on the sever repository (google code), since it is only populated in the user's local pc, at checkout time


### Summary of the available Ant Targets for each platform ###
This section summarizes what it is possible to do automatically with Ant on each platform. As it is possible to see from the table below, the Tag operation can be performed for every project.

|Project|Build|Test|Deploy/Release|Tag|
|:------|:----|:---|:-------------|:--|
|Java   |X    |X   |X             |X  |
|Android|     |`*` |              |X  |
|Eclipse|     |`*` |              |X  |
|.NET   |X    |    |X             |X  |

X = there is an Ant Target to do it

`<empty>` = there is NOT an Ant Target to do it

`*` = tests for Android and the Plugin do not exist