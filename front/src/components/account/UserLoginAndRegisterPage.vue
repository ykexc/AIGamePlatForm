<template>
  <div class="box container" style="margin-top: 10vh">
    <div class="pre-box">
      <h1>WELCOME</h1>
      <p style="font-size: 20px">AI GAME PLATFORM</p>
      <div class="btn-box-t">
        <button class="btn-box-b" :style="{backgroundColor: color}" @click="Switch">{{ text }}</button>
      </div>
    </div>
    <div class="register-form">
      <div class="title-box">
        <h1 class="form-h1-text bounce-top">注册</h1>
      </div>
      <div class="input-box">
        <input v-model="registerUser.username" type="text" placeholder="用户名">
        <input v-model="registerUser.password" type="password" placeholder="密码"/>
        <input
            v-model="registerUser.repeatPassword"
            type="password"
            placeholder="确认密码"
        />
      </div>
      <div class="btn-box">
        <button class="common" @click="register">注册</button>
      </div>
    </div>

    <div class="login-form">
      <div class="title-box">
        <h1 class="form-h1-text bounce-top">登录</h1>
      </div>
      <div class="input-box">
        <input v-model="loginUser.username" type="text" placeholder="用户名"/>
        <input v-model="loginUser.password" type="password" placeholder="密码"/>
      </div>
      <div class="btn-box">
        <button @click="login">登录</button>
      </div>
      <div class="other-login">
        ----------------第三方登录----------------
      </div>
      <div id="other-login-qq">
        <img @click="qqLogin" src="@/assets/img/QQ.png" alt="QQ_LOGIN"/>
      </div>
    </div>
  </div>
  <div style="margin-top: 15vh; text-align: center;">
    <img src="@/assets/img/beian.png" alt="公安备案"/>
    <a class="link-style" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=61042402000127">陕公网安备
      61042402000127号</a>

    |
    <a class="link-style" href="https://beian.miit.gov.cn" target="_blank">
      陕ICP备2023000609号-1</a>
    <p style="line-height: 2;color: #d79595">All rights reserved.</p>
  </div>

</template>

<script setup lang="ts">
import {computed, reactive, ref} from 'vue'
import $ from 'jquery'
import userUseStore from "@/stores/user";
import router from "@/router";
import {ElMessage, ElNotification} from "element-plus";
import {get, post} from "@/util/request";

const store = userUseStore()

interface LoginForm {
  username: string
  password: string
}

interface RegisterForm {
  username: string
  password: string
  repeatPassword: string
}

const loginUser = reactive<LoginForm>({
  username: '',
  password: ''
})
const flag2 = ref<boolean>(true)
const registerUser = reactive<RegisterForm>({
  username: '',
  password: '',
  repeatPassword: ''
})

const jwt_token = localStorage.getItem("jwt_token");
if (jwt_token) {
  store.UpdateToken(jwt_token)
  store.getInfo({
    success() {
      router.push('/snake/pk')
      store.UpdatePullingInfo(false)
    },
    error() {
      store.UpdatePullingInfo(false)
    }
  })
} else {
  store.UpdatePullingInfo(false)
}

let flag1: boolean = true
const color = computed(() => {
  if (flag2.value) return "#c9e0ed"
  else return "#edd4dc"
})

const text = computed(() => {
  if (flag2.value) return "SIGN UP"
  else return "SIGN IN"
})

const login = (): void => {
  if (!loginUser.username || !loginUser.password) {
    ElMessage.warning("请填写用户名或密码")
  } else {
    post({
      url: '/api/user/account/token/',
      data: {
        username: loginUser.username,
        password: loginUser.password
      },
      isAuth: false,
      success: (data): void => {
        if (data.error_message === "success") {
          localStorage.setItem("jwt_token", data.token)
          store.UpdateToken(data.token)
          store.getInfo({
            success: (): void => {
              router.push('/')
              ElNotification({
                title: 'Success',
                message: '欢迎回来',
                type: 'success',
                duration: 1000,
                offset: 100
              })
            },
            error: function (): void | null {
              ElMessage.error("系统错误")
            }
          })
        } else {
          ElMessage.error("用户名或密码错误")
        }
      }, error: (): void => {
        ElMessage.error("用户名或密码错误")
      }
    })
  }
}

const register = () => {
  post({
    url: '/api/user/account/register/',
    data: {
      username: registerUser.username,
      password: registerUser.password,
      confirmedPassword: registerUser.repeatPassword
    },
    isAuth: false,
    success: (data) => {
      if (data.error_message === "success") {
        localStorage.setItem("jwt_token", data.token)
        store.UpdateToken(data.token)
        ElNotification({
          title: 'Success',
          message: '欢迎来到 AI GAME PLATFORM',
          type: 'success',
          duration: 1000,
          offset: 100
        });
        router.push('/')
      } else {
        ElMessage.warning(data.error_message)
      }
    }
  })
}

const qqLogin = () => {
  get({
    url: '/api/user/account/qq/apply_code/',
    isAuth: false,
    success: (resp) => {
      if (resp.result === "success") {
        window.location.replace(resp.apply_code_url)
      }
    }
  })
}


const Switch = () => {
  const preBox = $('.pre-box')
  if (flag1) {
    preBox.css("transform", "translateX(100%)");
    preBox.css("background-color", "#c9e0ed");
  } else {
    preBox.css("transform", "translateX(0%)");
    preBox.css("background-color", "#edd4dc");
  }
  flag1 = !flag1
  flag2.value = !flag2.value
}
</script>

<style scoped>


:root {
  /* COLORS */
  --white: #e9e9e9;
  --gray: #333;
  --blue: #0367a6;
  --lightblue: #008997;

  /* RADII */
  --button-radius: 0.7rem;

  /* SIZES */
  --max-width: 758px;
  --max-height: 420px;

  font-size: 16px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
  Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
}

.other-login {
  margin-top: 20px;
  color: #5b5b5b;
  display: flex;
  justify-content: center;
  justify-items: center;
}

#other-login-qq {
  margin-top: 5px;
}

#other-login-qq > img {
  width: 30px;
  margin: 0 auto;
  display: block;
  cursor: pointer;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.form-h1-text {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 去除input的轮廓 */
input {
  outline: none;
}

.box {
  height: 100%;
}

.box {
  overflow-x: hidden;
  display: flex;
  background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
}

span {
  position: absolute;
  z-index: 0;
  bottom: 0;
  border-radius: 50%;
  /* 径向渐变 */
  background: radial-gradient(
      circle at 72% 28%,
      #fff 3px,
      #ff7edf 8%,
      #5b5b5b,
      #aad7f9 100%
  );
  /* 动画 */
  animation: myMove 4s linear infinite;
}

@keyframes myMove {
  0% {
    transform: translateY(0%);
    opacity: 1;
  }

  50% {
    transform: translate(10%, -1000%);
  }

  75% {
    transform: translate(-20%, -1200%);
  }

  99% {
    opacity: 0.9;
  }

  100% {
    transform: translateY(-1800%) scale(1.5);
    opacity: 0;
  }
}

.box {
  width: 1050px;
  height: 600px;
  display: flex;
  position: relative;
  top: 50px;
  z-index: 2;
  margin: auto;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.6);
  box-shadow: 2px 1px 19px rgba(0, 0, 0, 0.1);
}

.pre-box {
  width: calc(1050px / 2);
  height: 100%;
  position: absolute;
  left: 0;
  top: 0;
  z-index: 99;
  border-radius: 4px;
  background-color: #edd4dc;
  box-shadow: 2px 1px 19px rgba(0, 0, 0, 0.1);
  transition: 0.5s ease-in-out;
}

.pre-box h1 {
  margin-top: 150px;
  text-align: center;
  letter-spacing: 5px;
  color: white;
  user-select: none;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, 0.1);
}

.pre-box p {
  height: 30px;
  line-height: 30px;
  text-align: center;
  margin: 20px 0;
  user-select: none;
  font-weight: bold;
  color: white;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, 0.1);
}

.img-box img {
  width: 100%;
  transition: 0.5s;
}

.login-form,
.register-form {
  flex: 1;
  height: 100%;
}

.title-box {
  height: 250px;
  line-height: 500px;
}

.title-box h1 {
  text-align: center;
  color: white;
  user-select: none;
  letter-spacing: 5px;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, 0.1);
}

.input-box {
  display: flex;
  flex-direction: column;
  align-items: center;
}

input {
  width: 60%;
  height: 40px;
  margin-bottom: 20px;
  text-indent: 10px;
  border: 1px solid #fff;
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 120px;
  backdrop-filter: blur(10px);
}

input:focus::placeholder {
  opacity: 0;
}

h1 {
  display: block;
  font-size: 2em;
  margin-block-start: 0.67em;
  margin-block-end: 0.67em;
  margin-inline-start: 0;
  margin-inline-end: 0;
  font-weight: bold;
}

.btn-box {
  display: flex;
  justify-content: center;
}

.btn-box-t {
  display: flex;
  justify-content: center;
  padding-top: 50px;
}

.btn-box-b {
  width: 300px;
  height: 70px;
  border-radius: 30px;
  font-size: 20px;
  text-shadow: white;
  font-weight: bold;
}


button {
  width: 100px;
  height: 30px;
  margin: 0 7px;
  line-height: 30px;
  border: none;
  border-radius: 4px;
  background-color: #69b3f0;
  color: white;
}

button:hover {
  cursor: pointer;
  opacity: 0.8;
}

.btn-box p {
  height: 30px;
  line-height: 30px;
  user-select: none;
  font-size: 14px;
  color: #426277;
}

.btn-box p:hover {
  cursor: pointer;
  border-bottom: 1px solid #426277;
}

.bounce-top {
  -webkit-animation: bounce-top 0.9s both;
  animation: bounce-top 0.9s both;
}

@-webkit-keyframes bounce-top {
  0% {
    -webkit-transform: translateY(-45px);
    transform: translateY(-45px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
    opacity: 1;
  }
  24% {
    opacity: 1;
  }
  40% {
    -webkit-transform: translateY(-24px);
    transform: translateY(-24px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  65% {
    -webkit-transform: translateY(-12px);
    transform: translateY(-12px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  82% {
    -webkit-transform: translateY(-6px);
    transform: translateY(-6px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  93% {
    -webkit-transform: translateY(-4px);
    transform: translateY(-4px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  25%,
  55%,
  75%,
  87% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  100% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
    opacity: 1;
  }
}

@keyframes bounce-top {
  0% {
    -webkit-transform: translateY(-45px);
    transform: translateY(-45px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
    opacity: 1;
  }
  24% {
    opacity: 1;
  }
  40% {
    -webkit-transform: translateY(-24px);
    transform: translateY(-24px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  65% {
    -webkit-transform: translateY(-12px);
    transform: translateY(-12px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  82% {
    -webkit-transform: translateY(-6px);
    transform: translateY(-6px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  93% {
    -webkit-transform: translateY(-4px);
    transform: translateY(-4px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  25%,
  55%,
  75%,
  87% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  100% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
    opacity: 1;
  }
}

@-webkit-keyframes wobble-hor-bottom {
  0%,
  100% {
    -webkit-transform: translateX(0%);
    transform: translateX(0%);
    -webkit-transform-origin: 50% 50%;
    transform-origin: 50% 50%;
  }
  15% {
    -webkit-transform: translateX(-30px) rotate(-6deg);
    transform: translateX(-30px) rotate(-6deg);
  }
  30% {
    -webkit-transform: translateX(15px) rotate(6deg);
    transform: translateX(15px) rotate(6deg);
  }
  45% {
    -webkit-transform: translateX(-15px) rotate(-3.6deg);
    transform: translateX(-15px) rotate(-3.6deg);
  }
  60% {
    -webkit-transform: translateX(9px) rotate(2.4deg);
    transform: translateX(9px) rotate(2.4deg);
  }
  75% {
    -webkit-transform: translateX(-6px) rotate(-1.2deg);
    transform: translateX(-6px) rotate(-1.2deg);
  }
}

@keyframes wobble-hor-bottom {
  0%,
  100% {
    -webkit-transform: translateX(0%);
    transform: translateX(0%);
    -webkit-transform-origin: 50% 50%;
    transform-origin: 50% 50%;
  }
  15% {
    -webkit-transform: translateX(-30px) rotate(-6deg);
    transform: translateX(-30px) rotate(-6deg);
  }
  30% {
    -webkit-transform: translateX(15px) rotate(6deg);
    transform: translateX(15px) rotate(6deg);
  }
  45% {
    -webkit-transform: translateX(-15px) rotate(-3.6deg);
    transform: translateX(-15px) rotate(-3.6deg);
  }
  60% {
    -webkit-transform: translateX(9px) rotate(2.4deg);
    transform: translateX(9px) rotate(2.4deg);
  }
  75% {
    -webkit-transform: translateX(-6px) rotate(-1.2deg);
    transform: translateX(-6px) rotate(-1.2deg);
  }
}

.third-party-login > p > img {
  height: 4vh;
  margin-right: 1vw;
  cursor: pointer;
}

.link-style {
  text-decoration: none;
  color: #d79595;
}
</style>