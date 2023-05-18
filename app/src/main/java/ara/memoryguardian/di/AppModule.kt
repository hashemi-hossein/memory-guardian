package ara.memoryguardian.di

import android.content.ClipboardManager
import android.content.Context
import ara.memoryguardian.getClipboardManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager =
         context.getClipboardManager()
}
