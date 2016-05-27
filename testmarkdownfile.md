### General
Commands are case-sensitive. All commands are in lower case.

### Abbreviations
Some commands and keywords may be written in several different ways. For example, `create` may also be written `cr` and `breakpoint` may also be written `bp`. If you want to use the command `create breakpoint`, then, you may write any of the following four combinations:
* `create breakpoint`
* `create bp`
* `cr breakpoint`
* `cr bp`.

When such abbreviations are possible for a command, they are listed on a second line in the syntax description in their abbreviated form. Not all possible combinations are written out in this document but all possible abbreviations are mentioned.

## Informational commands
### hello
Syntax: `hello`

The `hello` command prints a simple text message. This is a self-test command that should always work. If it doesn't, there must be some error in JPF Inspector or your JPF installation.
### help
Syntax: `help`

The `help` command lists possible commands you can use. It does not print full descriptions, though, so you may want to refer to this wiki page for details. Also, commands do not automatically register into the `help` command so it is possible some newer commands will not be document in the help message.
### quit
Syntax: `quit` or `exit`

The `quit` command terminates the running system process via a `System.exit(0)` call. This will close the Inspector, the shell and the running JPF instance non-gracefully.
### print
Syntax: `print [expression]`

Prints the value of the given expression. See [[Expression syntax]] for details.

TODO: add the expression syntax here also.

## Breakpoint-related commands
### create breakpoint
Syntax: 
```
create breakpoint [property1, property2, ...] [hit condition]
cr bp [property1, property2, ...] [hit condition]
```
Creates a new breakpoint with the specified properties that suspends the system under test when the specified `hit condition` becomes true. See [[Hit Condition Expression Syntax]] for details on how to specify a breakpoint's hit condition. The hit condition is mandatory but properties are not.

You may set zero or more properties for each breakpoint. A single property may not repeat. These are the possible properties:

* `name = [identifier]` sets the breakpoint's name for ease of debugging; the name must start with a letter or an underscore and must contain only letters, digits and underscores.
* `state = [disable|enable|log]` sets the breakpoint's initial state. Only an enabled breakpoint stops the program upon being hit. See [[Breakpoints (advanced guide)]] for details.
* `state = [dis|en|log]` does the same as above.
* Hit count check. When you set a hit count check, the breakpoint will only perform its action (stopping the program or logging the result) if the hit count check condition is met. You may set the conditions like this:
  * `[minimum] <= hit_count` will trigger if the breakpoint was hit at least `minimum` times.
  * `[minimum] < hit_count` will trigger if the breakpoint was hit more than `minimum` times.
  * `hit_count <= [maximum]` will trigger if the breakpoint was hit at most `maximum` times.
  * `hit_count < [maximum]` will trigger if the breakpoint was hit less than `maximum` times.
  * You may also use `hc` instead of `hit_count`.
  * `[minimum] <= hc <= [maximum]` will trigger if the breakpoint was hit at least `minimum` and at most `maximum` times. Other combinations of operators are also possible. Only `<=` and `<` are valid operators.
  * This hit count checks considers the path hit count only.

Examples:
```
create breakpoint name=testing_breakpoint (hit condition)
create breakpoint name=testing_breakpoint state=en (hit condition)
cr breakpoint state=disable 20<=hit_count < 50 (hit condition)
```
