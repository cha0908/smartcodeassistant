<template>
  <div id="app">
    <SmartCodeAssistant />
  </div>
</template>

<script>
import SmartCodeAssistant from "./components/SmartCodeAssistant.vue";

export default {
  name: "App",
  components: {
    SmartCodeAssistant,
  },
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  height: 100%;
  overflow: hidden;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100vh;
  width: 100%;
  overflow: hidden;
}
</style>
