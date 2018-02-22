# How to Import Project to Eclipse

Unfortunately because we are building a Dynamic Web Project, we cannot easily import it into Eclipse.

We have to actually create a new Dynamic Web Project, and then copy all the contents over. Git makes this easy, however.

## Create a new Eclipse workspace [OPTIONAL]

    $ cd eclipse_workspace

## Create new Dynamic Web Project in Eclipse

Make sure that the name of the project is equivalent to the value of the `<name>` tag in [`.project`](./.project).

The filesystem should have created a folder in your eclipse workspace.

    $ ls eclipse_workspace/<project_name>

## Set up Git and pull from upstream

    $ cd eclipse_workspace/<project_name>
    $ git init
    $ git remote add origin git@github.com:nmits/CSCI310.git
    $ git add .
    $ git pull origin master
    $ git reset .

## Work from Eclipse, Commit from Command Line!

Your workspace contents should be in sync with Github now!
