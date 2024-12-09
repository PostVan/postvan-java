# PostVan

PostVan is an open-source library that enables you to execute [Postman](https://www.postman.com/) collections programmatically within your applications. It is designed to integrate seamlessly with web applications, providing a versatile solution for configuration-defined HTTP/HTTPS calls, API endpoint testing, and data extraction from APIs.

## Features

- **Execute Postman Collections**: Run Postman collections directly within your application.
- **Configuration-Driven**: Define HTTP/HTTPS calls via Postman’s collection JSON format.
- **API Endpoint Testing**: Automate API testing workflows for better reliability and debugging.
- **Data Extraction**: Extract and manipulate data from API responses programmatically.
- **Customizable Workflows**: Build complex workflows by chaining requests and handling dynamic data.

## Use Cases

- Automated API testing within CI/CD pipelines.
- Backend integration for web applications requiring complex API workflows.
- Data scraping or API-driven data extraction.
- Dynamic request generation based on runtime conditions.

## Getting Started

### Prerequisites

- **Java** (>=17 required)
- Postman collections in JSON format (exported from Postman).

### Installation

Install PostVan via npm:

```bash
npm install postvan
```

### Basic Usage

Here’s how to execute a Postman collection using PostVan:

```java
const PostVan = require('postvan');

// Load your Postman collection JSON file
const collection = require('./path/to/your-collection.json');

// Initialize PostVan
const postVan = new PostVan();

// Execute the collection
postVan.run(collection)
  .then((result) => {
    console.log('Execution completed successfully:', result);
  })
  .catch((error) => {
    console.error('Error during execution:', error);
  });
```

### Advanced Configuration

PostVan allows you to customize execution with options like environment variables, request hooks, and response handlers:

```java
postVan.run(collection, {
  environment: { apiKey: 'your-api-key' },
  preRequest: (req) => {
    console.log('Before request:', req);
  },
  postResponse: (res) => {
    console.log('After response:', res);
  },
});
```

## Documentation

Full documentation is available [here](https://github.com/yourusername/PostVan/wiki).

## Contributing

We welcome contributions from the community! Please read our [contributing guide](CONTRIBUTING.md) for details on how to get started.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Support

For questions or issues, please open an issue on our [GitHub repository](https://github.com/yourusername/PostVan/issues).

---

Make HTTP/HTTPS interactions easier, more efficient, and entirely customizable with **PostVan**!
