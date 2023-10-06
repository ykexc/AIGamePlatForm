<template>
  <NavaBar/>
  <el-scrollbar height="700px">
    <div class="game-hall">
      <el-card>
        <el-row :gutter="4">
          <el-col :span="6">
            <el-image
                :src="Image.snake"
                alt="蛇蛇大作战"
            />
          </el-col>
          <el-col :span="18">
            <el-card>
              <n-h1 style="text-align: center">
                <n-text type="success">
                  绕蛇
                </n-text>
              </n-h1>
              <n-p>
                <n-text style="font-size: 20px; font-weight: bold">
                  两名玩家同时操控两条蛇,蛇的初始长度为13,当一名玩家撞到墙体或另一名玩家的身体上或离线,对方获胜。
                </n-text>
              </n-p>
            </el-card>
            <div style="display: flex; justify-content: center; align-items: center; margin-top: 3vh">

              <n-icon size="40">
                <game-controller-outline/>
              </n-icon>
              <n-icon size="40" color="#0e7a0d">
                <game-controller/>
              </n-icon>
              <n-icon size="40" :component="GameController"/>
              <el-button type="primary" size="large" style="margin-left: 15px;margin-right: 15px" @click="toSnakeGame">
                点 我 创 建 游 戏
              </el-button>
              <n-icon size="40" :component="GameController"/>
              <n-icon size="40" color="#0e7a0d">
                <game-controller/>
              </n-icon>
              <n-icon size="40">
                <game-controller-outline/>
              </n-icon>
            </div>
          </el-col>
        </el-row>
      </el-card>
      <el-card style="margin-top: 5vh">
        <el-row :gutter="4">
          <el-col :span="6">
            <el-image
                :src="Image.gobang"
            />
          </el-col>
          <el-col :span="18">
            <el-card>
              <n-h1 style="text-align: center">
                <n-text type="success">
                  五子棋
                </n-text>
              </n-h1>
              <n-p>
                <n-text style="font-size: 20px; font-weight: bold">
                  两名玩家分别代表白方和黑方,黑先、白后，交替下子，每次只能下一子,若自己离线或对方有五个棋子在同一直线上则对方获胜
                </n-text>
              </n-p>
            </el-card>
            <div style="display: flex; justify-content: center; align-items: center; margin-top: 3vh">

              <n-icon size="40">
                <game-controller-outline/>
              </n-icon>
              <n-icon size="40" color="#0e7a0d">
                <game-controller/>
              </n-icon>
              <n-icon size="40" :component="GameController"/>
              <el-button type="primary" size="large" style="margin-left: 15px;margin-right: 15px" @click="toGoBangGame">
                点 我 创 建 游 戏
              </el-button>
              <n-icon size="40" :component="GameController"/>
              <n-icon size="40" color="#0e7a0d">
                <game-controller/>
              </n-icon>
              <n-icon size="40">
                <game-controller-outline/>
              </n-icon>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </el-scrollbar>


</template>

<script setup lang="ts">
import Image from "@/enums/Image";
import {GameControllerOutline, GameController} from '@vicons/ionicons5'
import router from "@/router";
import NavaBar from "@/components/NavaBar.vue";

const toSnakeGame = () => {
  router.push('/snake/pk')
}
const toGoBangGame = () => {
  router.push('/gobang/pk')
}

</script>

<style scoped>

.game-hall {
  margin: 6vh 38vh;
}

</style>