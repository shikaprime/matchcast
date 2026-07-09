package com.example.matchcast.presentaion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.matchcast.data.local.AppDatabase
import com.example.matchcast.data.repository.MatchRepositoryImpl
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(this)
        val matchDao = database.matchDao()
        val repository = MatchRepositoryImpl(matchDao)

        lifecycleScope.launch {
            try {
                Log.d("API_CHECK", "Запуск загрузки матчей с сервера...")
                repository.refreshMatch()
                Log.d("API_CHECK", "Сеть отработала успешно")
            }catch (e: Exception){
                Log.e("API_CHECK", "Ошибка при обновлении матчей: ${e.message}",e)
            }
        }
        lifecycleScope.launch {
            matchDao.getMatches().collect {
                matchDao -> Log.d("DB_CHECK", "база данных обновилась, всего данных в кеше: ${matchDao.size}")
                matchDao.forEach { matchEntity -> Log.d("DB_CHECK", "Матч из БД: ${matchEntity.homeTeam} - ${matchEntity.awayTeam} (${matchEntity.dateUtc})") }
            }
        }
    }
}