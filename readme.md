# What is LSDT?
This repository contains a lightweight SQL-like database (LSDT) implementation.
LSDT currently saves the table data in a memory efficient tightly packed binary format and indexes the tables in a seperate file.


Currently the implementation only contains very basic database methods but further more usefull methods are planned.

## What can LSDT do?

The currently supported methods include:

- nt $name$($typ $name,$typ $name,..)
  
  The new table (nt) command adds a new table with the specified name and columns to the database.

- dt $name$
  
  The delete table (dt) command removes a table from the database.

- it $name$ $value $value
  
  The insert table (it) command inserts data into an existing table.

- lt

  The list table (lt) command prints the contents of the exisiting tables to the console.

## How can I use it?

Clone the respository and use either maven or ant to compile and build the source files. Try out any of the above commands.
