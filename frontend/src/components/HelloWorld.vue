<script setup lang="ts">
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from '@/components/ui/dropdown-menu';
import { Icon } from '@iconify/vue';
import { useColorMode } from '@vueuse/core';
import { ref } from 'vue';

defineProps<{ msg: string }>()

const count = ref(3)
// Pass { disableTransition: false } to enable transitions
const mode = useColorMode()
</script>

<template>
  <Card class="w-[350px]">
    <CardHeader>
      <CardTitle>{{ msg }}</CardTitle>
      <CardDescription>Coolest combo ever</CardDescription>
    </CardHeader>
    <CardContent>
      <div class="flex items-center justify-between">
        <p class="leading-7">
          Beutiful components
        </p>
        <Button @click="count++">count is {{ count }}</Button>
      </div>
      <p class="leading-7 mt-6">
        Edit
        <code>components/HelloWorld.vue</code> to test HMR
      </p>
      <p class="leading-7 mt-6">
        Check out
        <a href="https://vuejs.org/guide/quick-start.html#local" target="_blank">create-vue</a>, the official Vue + Vite
        starter
      </p>
      <p class="leading-7 mt-6">
        Learn more about IDE Support for Vue in the
        <a href="https://vuejs.org/guide/scaling-up/tooling.html#ide-support" target="_blank">Vue Docs Scaling up
          Guide</a>.
      </p>
      <div class="flex justify-between mt-6">
        <p class="leading-7">
          It has dark mode support!
        </p>
        <DropdownMenu>
          <DropdownMenuTrigger as-child>
            <Button variant="outline">
              <Icon icon="radix-icons:moon"
                class="h-[1.2rem] w-[1.2rem] rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
              <Icon icon="radix-icons:sun"
                class="absolute h-[1.2rem] w-[1.2rem] rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
              <span class="sr-only">Toggle theme</span>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            <DropdownMenuItem @click="mode = 'light'">
              Light
            </DropdownMenuItem>
            <DropdownMenuItem @click="mode = 'dark'">
              Dark
            </DropdownMenuItem>
            <DropdownMenuItem @click="mode = 'auto'">
              System
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </CardContent>
    <CardFooter class="px-6 pb-6">
      <p class="leading-7 text-sm text-muted-foreground">
        Click on the Vite / Vue / Shadcn-Vue logos to learn more
      </p>
    </CardFooter>
  </card>

</template>
