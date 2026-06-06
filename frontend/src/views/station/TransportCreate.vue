<!-- 模块3：无人机转运与接口监控。 -->
<template>
  <div class="page">
    <PageHeader title="发起无人机转运任务"/>
    <el-card>
      <el-form :model="form" label-width="140px">
        <el-form-item label="回收站点">
          <el-select v-model="form.stationId" filterable @change="syncStartPoint">
            <el-option
                v-for="station in stations"
                :key="station.id"
                :label="station.stationName"
                :value="station.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="转运起点">
          <el-select v-model="form.startPoint" filterable>
            <el-option
                v-for="station in stations"
                :key="station.id"
                :label="station.stationName"
                :value="station.stationName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="转运终点">
          <el-select v-model="form.endPoint" filterable>
            <el-option
                v-for="point in endPointOptions"
                :key="point"
                :label="point"
                :value="point"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="废油类型">
          <el-select v-model="form.oilType">
            <el-option label="家用植物油" value="HOUSEHOLD"/>
            <el-option label="餐饮废油" value="RESTAURANT"/>
          </el-select>
        </el-form-item>
        <el-form-item label="转运重量(kg)">
          <el-input-number v-model="form.weight" :min="1"/>
        </el-form-item>
        <el-form-item label="任务优先级">
          <el-select v-model="form.priority">
            <el-option label="普通" value="NORMAL"/>
            <el-option label="高优先级" value="HIGH"/>
          </el-select>
        </el-form-item>
        <el-button type="primary" :loading="submitting" :disabled="submitting" @click="submit">创建任务</el-button>
        <el-button type="success" :disabled="!created" @click="push">推送至无人机平台</el-button>
      </el-form>
      <el-alert v-if="created" style="margin-top:16px" type="success" :closable="false" show-icon>
        <template #title>已创建转运任务：{{ created.taskNo }}，状态：{{ enumLabel(created.status) }}</template>
      </el-alert>
    </el-card>
  </div>
</template>
<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue'
import PageHeader from '@/components/PageHeader.vue'
import {stationApi, transportApi} from '@/api'
import {ElMessage} from 'element-plus'
import {enumLabel} from '@/utils/dict'

// 创建后的转运任务。
const created = ref<any>()
const submitting = ref(false)
const stations = ref<any[]>([])
const endPointOptions = ['集中处理中心', '无害化处理中心', '资源化利用中心']
// 转运任务表单，枚举值传后端，展示文本使用中文。
const form = reactive({
  stationId: 1,
  startPoint: '东区绿色回收站',
  endPoint: '集中处理中心',
  oilType: 'HOUSEHOLD',
  weight: 20,
  priority: 'NORMAL',
  remark: ''
})
// 创建转运任务。
const submit = async () => {
  if (submitting.value) return
  submitting.value = true
  try {
    created.value = await transportApi.create(form);
    ElMessage.success('转运任务创建成功：' + created.value.taskNo)
  } finally {
    submitting.value = false
  }
}
// 模拟推送给无人机调度平台。
const push = async () => {
  created.value = await transportApi.push(created.value.id);
  ElMessage.success('已推送至模拟无人机调度平台')
}
const syncStartPoint = () => {
  const station = stations.value.find((item: any) => item.id === form.stationId)
  if (station) form.startPoint = station.stationName
}
onMounted(async () => {
  const data: any = await stationApi.page({page: 1, size: 100})
  stations.value = data.records || []
  if (!stations.value.some((station: any) => station.id === form.stationId) && stations.value[0]) {
    form.stationId = stations.value[0].id
  }
  syncStartPoint()
})
</script>
