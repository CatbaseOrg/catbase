<script setup lang="ts">
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <div class="flex flex-col items-center justify-center min-h-screen py-2">
    <div class="flex items-center justify-center">
      <a href="https://vitejs.dev" target="_blank">
        <img src="/vite.svg" class="logo" alt="Vite logo" />
      </a>
      <a href="https://vuejs.org/" target="_blank">
        <img src="./assets/vue.svg" class="logo vue" alt="Vue logo" />
      </a>
      <a href="https://shadcn-vue.com/" target="_blank">
        <img src="/shadcn-vue.svg" class="logo shad" alt="Shadcn-Vue logo" />
      </a>
    </div>
    <div class="flex items-center justify-center mt-4">
      <HelloWorld msg="Vite + Vue + Shadcn-Vue + TS" />
    </div>
  </div>
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}

.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}

.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}

.logo.shad:hover {
  filter: drop-shadow(0 0 1em #42b883);
}
</style>
