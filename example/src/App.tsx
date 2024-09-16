import { useEffect, useState } from 'react';
import { StyleSheet, View, Text, Button } from 'react-native';
import { addVolumeListener } from 'react-native-volume-listener';

export default function App() {
  const [listening, setListening] = useState(false);
  const [volume, setVolume] = useState<number | null>(null);

  useEffect(() => {
    if (listening) {
      const listener = addVolumeListener((result) => {
        setVolume(result.volume);
      });
      return () => {
        listener.remove();
      };
    }
    return;
  }, [listening]);

  return (
    <View style={styles.container}>
      <View style={styles.box}>
        <View>
          <Text>Volume:</Text>
          <Text>{volume ?? '-'}</Text>
        </View>
        <View>
          {listening ? (
            <Button
              onPress={() => setListening(false)}
              title="Stop Listening"
            />
          ) : (
            <Button
              onPress={() => setListening(true)}
              title="Start Listening"
            />
          )}
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 100,
    marginVertical: 20,
    gap: 20,
  },
});
