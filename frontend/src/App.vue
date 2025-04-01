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
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
