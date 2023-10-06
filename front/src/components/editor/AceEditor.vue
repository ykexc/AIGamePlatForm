<template>
  <v-ace-editor
      v-model:value="codes"
      :lang=props.lang
      :theme=props.theme
      :options="options"
      style="width: 920px;height: 35vh"
  />
</template>

<script setup lang="ts">
import {VAceEditor} from "vue3-ace-editor"
import '@/composables/ace.config'
import {onMounted, ref, watch} from "vue";

interface Props {
  lang: string
  theme: string
  value: string
  options: object
}

const emit = defineEmits<{
  (e: 'updateValue', value: string): void
}>()

const props = withDefaults(defineProps<Props>(), {
  lang: 'c_cpp',
  theme: 'monokai',
  value: '',
  options: () => ({})
})

const codes = ref<string>(props.value)
onMounted(() => {
  codes.value = props.value
})
watch(codes, (_oldValue: string, _newValue: string) => {
  emit('updateValue', codes.value),
      {deep: true}
})

</script>


<style scoped>

</style>