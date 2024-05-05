# Code Quality Analyzer

## Overview

This application is a code quality analyzer designed to assess Java and Kotlin files within a
specified directory. It evaluates the complexity of methods by counting control flow statements and
checks adherence to the camelCase naming convention for method names.

## Features

- **Complexity Analysis:** Calculates complexity based on the presence of control flow statements
  like `if`, `for`, `while`, `switch`, `do`, `try`, `catch`, and `finally`.
- **Style Check:** Verifies that method names follow the camelCase naming convention.
- **Results Reporting:** Outputs the names and complexity scores of the three methods with the
  highest complexity and the percentage of methods not following camelCase.

## Requirements

- JDK 11 or later
- Kotlin 1.9.23
- Gradle (compatible with the version specified in `build.gradle.kts`)

## Setup and Installation

1. Ensure that Java Development Kit (JDK) is installed on your system. You can download it
   from [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or
   adopt a version from third-party distributions.
2. Clone this repository or download the source code to your local machine.
3. Navigate to the root directory of the project via command line.

## Building the Project

To build the project and compile the source files, run the following command from the root directory
of the project:

```bash
./gradlew build
```

## Running the Application

To run the application, use the following command, replacing `<directory-path>` with the path to the
directory containing your Java or Kotlin files:

```bash
./gradlew run --args='<directory-path>'
```

Ensure the directory path is absolute or relative to the root of the project.

## Running Tests

To execute all the tests, run:

```bash
./gradlew test
```

This will run the unit tests defined in the `CodeAnalyzerTests.kt` file and report the results.

## Design Decisions

- **Simple Complexity Metric:** The complexity of the code is assessed by counting control
  structures such as `if`, `for`, and `while`. This method provides a basic measure of code
  complexity, suitable for straightforward assessments.
- **Naming Convention Checks:** The application checks if method names adhere to the camelCase
  style. This feature helps promote consistent coding practices.
- **Modular Code Structure:** Functions are organized to perform specific tasks, such as reading
  files, analyzing methods, and reporting results. This organization aids in maintaining clarity and
  ease of updates.

