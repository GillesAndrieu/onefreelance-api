<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Code guidelines](#code-guidelines)
    - [Build and run](#build-and-run)

## Code guidelines

### Build and Run

    #/tools/docker> docker-compose up -d 
    #/api> gradle clean build
    #/api> gradle bootRun

or startup the application with make (for any problem, check the logs in `/tools/.log`) :

    #/tools>make bootstrap        (for the first launch)
    #/tools>make ide