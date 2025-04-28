<template>
  <div class="app">
    <header>
      <h1>주간 날씨 정보</h1>
    </header>
    <main>
      <div class="weather-container">
        <div class="location-input">
          <input v-model="nx" placeholder="위도(nx)" type="text">
          <input v-model="ny" placeholder="경도(ny)" type="text">
          <button @click="getWeatherData">날씨 조회</button>
        </div>
        <div v-if="weatherData" class="weather-info">
          <div v-for="(forecast, index) in processedWeatherData" :key="index" class="weather-card">
            <h3>{{ forecast.date }}</h3>
            <p>기온: {{ forecast.temperature }}°C</p>
            <p>강수확률: {{ forecast.rainProbability }}%</p>
            <p>습도: {{ forecast.humidity }}%</p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'App',
  data() {
    return {
      nx: '60',
      ny: '127',
      weatherData: null
    }
  },
  computed: {
    processedWeatherData() {
      if (!this.weatherData) return [];
      
      const items = this.weatherData.response?.body?.items?.item || [];
      const forecasts = {};
      
      items.forEach(item => {
        const date = item.fcstDate;
        if (!forecasts[date]) {
          forecasts[date] = {
            date: `${date.substring(4, 6)}/${date.substring(6, 8)}`,
            temperature: '-',
            rainProbability: '-',
            humidity: '-'
          };
        }
        
        switch (item.category) {
          case 'TMP':
            forecasts[date].temperature = item.fcstValue;
            break;
          case 'POP':
            forecasts[date].rainProbability = item.fcstValue;
            break;
          case 'REH':
            forecasts[date].humidity = item.fcstValue;
            break;
        }
      });
      
      return Object.values(forecasts);
    }
  },
  methods: {
    async getWeatherData() {
      try {
        const response = await axios.get(`http://localhost:8081/api/weather/forecast?nx=${this.nx}&ny=${this.ny}`);
        this.weatherData = response.data;
      } catch (error) {
        console.error('날씨 데이터를 가져오는데 실패했습니다:', error);
      }
    }
  }
}
</script>

<style>
.app {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

header {
  text-align: center;
  margin-bottom: 30px;
}

.location-input {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-bottom: 20px;
}

.location-input input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.location-input button {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.weather-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.weather-card {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
}

.weather-card h3 {
  margin-top: 0;
  color: #333;
}
</style>
