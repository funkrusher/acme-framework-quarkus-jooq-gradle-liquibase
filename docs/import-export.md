
Data can be imported/export from/to various data-formats.
- JSON
- XML
- CSV
- XLSX

For flat formats (CVS/XLSX) every line in the file contains exactly one "view" on a specific hierarchy.
For example:
- a file "product.csv" would only contain the data of the Product-table
- a file "product_lang.csv" would only contain the data of the ProductLang-table and the reference-id to the products.

In the hierarchical formats (XML,JSON) the file could also contain only one view of a specific hierarchy,
but the main-file could also complete the complete data of all possible hierarchies per one item.
- a file "product.xml" would contain the data of all Product-specific-tables
- (no additional files needed, as the product.xml could contain all data for products in one file)

This would be a standard import/export.
It is also possible to write formats that are specific for a clients needs instead,
but it's advised to define a standard import/export also, as basic functionality to all clients.



