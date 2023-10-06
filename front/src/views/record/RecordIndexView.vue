<template>
  <NavaBar/>
  <el-scrollbar>
    <div class="card-container">
      <el-card class="box-card" shadow="always">
        <template #header>
          <div class="card-header">
            <span class="gradient-text">对局列表</span>
          </div>
        </template>
        <el-table ref="table" :data="records" height="450px" table-layout="fixed" style="width: 100%">

          <el-table-column label="游戏">
            <template #default="scope">
              <div style="position: relative;display: inline-block; align-items: center">
                <div>
                  <img :src="scope.row.record.asx === null ? Image.gobangSmall : image.snakeSmall"
                       style="width: 40px;border-radius: 50%;" alt="game">
                  <div style="text-align: center; font-weight: bold;font-size: 1px">
                    {{ scope.row.record.asx !== null ? '绕蛇' : '五子棋' }}
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="玩家A">
            <template #default="scope">
              <div style="position: relative;display: inline-block; align-items: center">
                <div>
                  <img :src="scope.row.a_photo" style="width: 40px;border-radius: 50%;" alt="Avatar">
                  <span
                      :style="{backgroundImage: `url(${scope.row.result === 'A胜'
                                     ? image.winner : scope.row.result === 'B胜' ? image.loser : image.tie})`}"
                      class="icon"></span>
                  <div style="text-align: center; font-weight: bold;font-size: 1px">{{ scope.row.a_username }}</div>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="玩家B">
            <template #default="scope">
              <div style="position: relative;display: inline-block; align-items: center">
                <div>
                  <img :src="scope.row.b_photo" style="width: 40px;border-radius: 50%;" alt="Avatar">
                  <span
                      :style="{backgroundImage: `url(${scope.row.result === 'B胜'
                                     ? image.winner : scope.row.result === 'A胜' ? image.loser : image.tie})`}"
                      class="icon"></span>
                  <div style="text-align: center; font-weight: bold;font-size: 1px">{{ scope.row.b_username }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="record.createTime" label="比赛时间"/>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button
                  size="small"
                  type="primary"
                  @click="showRecord(scope.row.record.id, scope.row.record.asx !== null)"
                  plain
              >查看回放
              </el-button
              >
            </template>

          </el-table-column>
        </el-table>
        <div style="display: flex">
          <div style="flex: 1"></div>
          <el-pagination
              style="margin-top: 5px"
              background
              layout="prev, pager, next"
              :total="recordsCount"
              hide-on-single-page
              :page-size="10"
              @current-change="getRecords"
              @next-click="handleCurrentChange"
              @prev-click="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>
  </el-scrollbar>

</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {get} from "@/util/request";
import image from "@/enums/Image";
import Image from "@/enums/Image";
import recordUseStore from '@/stores/snake/record'
import pkUseStore from '@/stores/snake/pk'
import router from "@/router";
import NavaBar from "@/components/NavaBar.vue";
import gobangRecordUseStore from '@/stores/gobang/record'
import gobangPkUseStore from '@/stores/gobang/pk'
const gobangPkStore = gobangPkUseStore()
const gobangRecordStore = gobangRecordUseStore()
const recordStore = recordUseStore()
const pkStore = pkUseStore()
const {updateGobangGame} = gobangPkStore
const {updateGame} = pkStore
const {updateIsRecord, updateSteps, updateRecordLoser} = recordStore
const {updateGoBangIsRecord, updateGoBangSteps, updateGoBangRecordLoser, updateIsRunning, updatePlayer} = gobangRecordStore

interface Record {
  id: number
  aId: number
  bId: number
  aSx: number
  bSx: number
  aSy: number
  bSy: number
  asteps: string
  bsteps: string
  map: string
  loser: string
  createTime: string
}

interface Item {
  result: string
  a_photo: string
  a_username: string
  b_photo: string
  b_username: string
  record: Record
}

const records = ref<Array<Item>>()
const recordsCount = ref(0)
const table = ref()


const handleCurrentChange = () => {
  table.value.$refs.scrollBarRef.setScrollTop(0)
}

const getRecords = (page: number): void => {
  get({
    url: '/api/record/getlist/',
    data: {
      page: page
    },
    isAuth: true,
    success: (data) => {
      recordsCount.value = data.records_count
      records.value = data.records
      handleCurrentChange()
    }
  })
}

const showRecord = (recordId: number, isKob: boolean): void => {
  if (records.value === undefined) return
  for (const r of records.value) {
    if (r.record.id === recordId) {
      if (isKob) {
        updateIsRecord(true)
        updateSteps({
          a_steps: r.record.asteps,
          b_steps: r.record.bsteps
        })
        updateGame({
          a_sx: r.record.aSx,
          a_sy: r.record.aSy,
          a_id: r.record.aId,
          b_sx: r.record.bSx,
          b_sy: r.record.bSy,
          b_id: r.record.bId,
          map: stringTo2D(r.record.map)
        })
        updateRecordLoser(r.record.loser)
        router.push({
          name: 'snake_record_content',
          params: {
            recordId: recordId
          }
        })
      } else {
        let emptyArray: Array<Array<number>> = Array.from({ length: 16 }, () =>
            Array(16).fill(0)
        );
        updateIsRunning(true)
        updatePlayer(r.a_username, r.b_username)
        updateGobangGame({
          aId: r.record.aId,
          bId: r.record.bId,
          gamemap: emptyArray
        })
        updateGoBangIsRecord(true)
        updateGoBangSteps({
          a_steps: strTo2D(r.record.asteps),
          b_steps: strTo2D(r.record.bsteps)
        })
        updateGoBangRecordLoser(r.record.loser)
        router.push({
          name: 'gobang_record_content',
          params: {
            recordId: recordId
          }
        })
      }
      break
    }
  }
}

function strTo2D(s: string): Array<Array<number>> {
  let ret: number[][] = []
  let oneRound: string[] = s.split("*");
  for (let oneStep of oneRound) {
    let xAndY = oneStep.split("#");
    let x = parseInt(xAndY[0]), y = parseInt(xAndY[1])
    ret.push([x, y])
  }
  return ret
}

function stringTo2D(s: string): Array<Array<number>> {
  let g: number[][] = [];
  for (let i = 0, k = 0; i < 13; i++) {
    let line: number[] = [];
    for (let j = 0; j < 14; j++, k++) {
      if (s[k] === '0') line.push(0);
      else line.push(1);
    }
    g.push(line);
  }
  return g;
}

onMounted(() => {
  getRecords(1)
})

</script>

<style scoped>


.card-container {
  display: flex;
  justify-content: center;
}


.card-header {
  display: flex;
  justify-content: center;
  align-items: center;
}

.box-card {
  margin-top: 50px;
  width: 80%;
  text-align: center;
}

.gradient-text {
  font-size: 24px;
  font-weight: bold;
  background: linear-gradient(45deg, #78f3c2, #4888a4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}


.icon {
  position: absolute;
  top: -5px; /* 调整位置 */
  left: 50%; /* 居中 */
  transform: translateX(50%); /* 居中 */
  display: inline-block;
  width: 30px;
  height: 30px;
  background-image: url("https://ykexc-1314584214.cos.ap-nanjing.myqcloud.com/loser.png"); /* 图标URL */
  background-size: cover;
}

</style>