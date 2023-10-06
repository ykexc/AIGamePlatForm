<template>
  <el-button type="info" :icon="Edit" @click="dialogFormVisible = true">修改</el-button>
  <el-dialog :append-to-body="true" top="8vh" v-model="dialogFormVisible">
    <template #header>
      <div class="gradient-text">修改Bot</div>
    </template>
    <el-form
        ref="form"
        :model="botForm"
        label-width="auto"
        label-position="top"
        size="default"
        :rules="rules"
    >
      <el-form-item prop="title" label="Bot名称">
        <el-input v-model="botForm.title" placeholder="请输入修改后的bot名称"/>
      </el-form-item>
      <el-form-item label="Bot描述">
        <el-input type="textarea" maxlength="300" show-word-limit v-model="botForm.description"/>
      </el-form-item>
      <el-form-item label="Bot语言">
        <el-select v-model="botForm.type">
          <el-option label="c++" value="c_cpp"/>
          <el-option label="java" value="java"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <template #label>
          <div style="display: flex">
            <div>Bot代码</div>
            <div style="flex: 1"></div>
            <div>
              <el-icon class="button" @click="innerVisible = true" size="15px">
                <Setting/>
                <el-dialog
                    v-model="innerVisible"
                    width="18%"
                    align-center
                    :modal="false"
                    append-to-body>
                  <template #header>
                    编辑器设置
                  </template>
                  <div>
                    <p>
                      主题
                      <el-select
                          v-model="themeStyle"
                          class="select"
                          size="small"
                          :placeholder="themeStyle"
                      >
                        <el-option
                            v-for="item in themes"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                      </el-select>
                    </p>
                    <p>
                      字体大小
                      <el-input-number
                          v-model="fontSize"
                          :min="15"
                          :max="30"
                          class="input"
                          size="small"
                          controls-position="right"
                      />
                    </p>
                  </div>
                </el-dialog>
              </el-icon>
            </div>
          </div>

        </template>
        <AceEditor :theme="themeStyle"
                   :lang="botForm.type"
                   :options="{ fontSize:fontSize, showPrintMargin: false }"
                   :value="botForm.content"
                   @update-value="updateValue"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateBot">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
//TODO  将style绑定到每个用户上
import {reactive, ref} from 'vue'
import {Edit, Setting} from "@element-plus/icons-vue";
import type {FormRules} from "element-plus";
import AceEditor from "@/components/editor/AceEditor.vue";
import {post} from "@/util/request";
import {ElMessage} from "element-plus";

interface BotForm {
  botId: number
  title: string
  description: string
  content: string
  type: string
  game: string
}

interface Theme {
  label: string
  value: string
}

const props = defineProps<BotForm>()
const emit = defineEmits<{
  (e: 'reFresh'): void
}>()
const dialogFormVisible = ref<boolean>(false)
const innerVisible = ref<boolean>(false)
const themeStyle = ref<string>('monokai')
const fontSize = ref<number>(15)
const rules = reactive<FormRules>({
  title: [
    {required: true, message: 'bot名称不能为空!', trigger: 'blur'}
  ]
})
const botForm = reactive<BotForm>({
  botId: props.botId,
  title: props.title,
  description: props.description,
  content: props.content,
  type: props.type,
  game: props.game
})
const themes: Array<Theme> = [
  {
    label: 'monokai',
    value: 'monokai'
  },
  {
    label: 'chrome',
    value: 'chrome'
  },
  {
    label: 'github',
    value: 'github'
  }
]

const updateValue = (value: string) => {
  botForm.content = value
}

const updateBot = () => {
  post({
    url: '/api/user/bot/update/',
    data: {
      bot_id: botForm.botId,
      type: botForm.type,
      description: botForm.description,
      content: botForm.content,
      title: botForm.title,
      game: botForm.game
    },
    isAuth: true,
    success: (data) => {
      if (data.error_message === 'success') {
        dialogFormVisible.value = false
        emit('reFresh')
        ElMessage.success("修改Bot成功")
      } else {
        ElMessage.error(data.error_message)
      }
    }
  })
}


</script>
<style scoped>


.dialog-footer button:first-child {
  margin-right: 10px;
}

.button {
  transition: all .2s ease-out; /* 添加过渡效果 */
}

.button:hover {
  cursor: pointer;
  transform: scale(1.5); /* 鼠标放上去后，将按钮放大1.1倍 */
}

.gradient-text {
  flex: 1;
  font-size: 24px;
  font-weight: bold;
  background: linear-gradient(45deg, #78f3c2, #4888a4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}


.select {
  width: auto;
  float: right;
}

.input {
  float: right;
}
</style>