# react-native-volume-listener

Listen to changes in volume output

## Installation

```sh
npm install @seventytwodays/react-native-volume-listener
```

## Usage

```js
import { useState, useEffect } from 'react';
import { addVolumeListener } from '@seventytwodays/react-native-volume-listener';

// ...
const [volume, setVolume] = useState(null);

useEffect(() => {
  const listener = addVolumeListener((vol) => {
    setVolume(vol);
  });
  return () => listener.remove();
}, []);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
