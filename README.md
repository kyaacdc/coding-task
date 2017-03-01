Offline coding task
===================================

Soon after joining *Enterprise Dinosaurs Inc.* you are tasked with updating their flag product - **OMS** (Order Management System).
This system has a lot of legacy poor quality code as you will soon see. Please also treat any comments you find in the code
with a grain of salt.

## OMS functionality

According to documentation you received, OMS is tasked with generating orders based on selected item and associated customer.
In the process it also calculates the tax due and sends it to the tax office system - the accurateness of this information
if vital for the company to be in compliance with tax regulations. Generated orders are queued (FIFO) in order of creation
(priority orders are exception - these will always be fetched immediately). Separate system will later fetch subsequent
orders for further processing. During this processing `process()` method of class `Order` will be called;
within this method important interactions with `SeriousEnterpriseEventBus` happen.
It is also stated in documentation that all calculations are done with help of centralized `TaxCalculationsHelper` to meet
tax office requirement of all taxes due amount to be rounded up to full USD cents.

## Your tasks:

1. Create capability to combine priority, discounted and international attributes within single order.
Traditionally these types were mutually exclusive but due to new business needs we would like to have capability to
have e.g. discounted priority order, discounter international order etc.
2. **Important!** Adhere to boy scout rule. Please refactor to improve quality and maintainability of OMS codebase.
3. Fix any obvious bug you find.


## Do not modify specific interfaces/classes:

These are not perfect but for compatibility reasons do not modify following interfaces:

+ OrdersManagementSystem
+ TaxOfficeAdapter
+ ItemsRepository
+ SeriousEnterpriseEventBus

class:
+ SeriousEnterpriseEventBusLookup
