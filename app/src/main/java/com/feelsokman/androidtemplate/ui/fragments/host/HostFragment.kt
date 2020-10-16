package com.feelsokman.androidtemplate.ui.fragments.host

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkManager
import com.feelsokman.androidtemplate.databinding.FragmentHostBinding
import com.feelsokman.androidtemplate.deadlocktest.ExecutorGuy
import com.feelsokman.androidtemplate.deadlocktest.RunBlockingMutexGuy
import com.feelsokman.androidtemplate.deadlocktest.SingleGuy
import com.feelsokman.androidtemplate.deadlocktest.SuspendMutexGuy
import com.feelsokman.androidtemplate.di.component.AppComponent
import com.feelsokman.androidtemplate.di.getComponent
import com.feelsokman.androidtemplate.ui.activity.viewmodel.MainViewModel
import com.feelsokman.androidtemplate.ui.base.BaseFragment
import com.feelsokman.androidtemplate.ui.fragments.host.viewmodel.HostViewModel
import com.feelsokman.androidtemplate.utilities.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import javax.inject.Inject

class HostFragment : BaseFragment() {

    @Inject internal lateinit var viewModelFactory: ViewModelFactory
    @Inject internal lateinit var workManager: WorkManager
    @Inject internal lateinit var singleGuy: SingleGuy
    @Inject internal lateinit var suspendMutexGuy: SuspendMutexGuy
    @Inject internal lateinit var blockingMutexGuy: RunBlockingMutexGuy
    @Inject internal lateinit var executorGuy: ExecutorGuy
    private val viewModelHost by viewModels<HostViewModel> { viewModelFactory }
    private val activityViewModel by activityViewModels<MainViewModel>()

    private lateinit var viewBinder: ViewBinder

    private var _binding: FragmentHostBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        injectDependencies(context)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.singleFromDifferentThreads.setOnClickListener {
            lifecycleScope.launch {
                val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
                scope.launch {
                    val jobs: List<Job> = 1.rangeTo(100).map {
                        launch {
                            singleGuy.getAuthToken()
                        }
                    }

                    jobs.forEach {
                        it.join() // wait for all coroutines to finish
                    }
                }.join()
            }
        }

        binding.singleUiThread.setOnClickListener {
            lifecycleScope.launch {
                val jobs: List<Job> = 1.rangeTo(100).map {
                    launch {
                        singleGuy.getAuthToken()
                    }
                }

                jobs.forEach {
                    it.join() // wait for all coroutines to finish
                }
            }
        }

        binding.executor.setOnClickListener {
            lifecycleScope.launch {
                val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
                scope.launch {
                    val jobs: List<Job> = 1.rangeTo(100).map {
                        launch {
                            executorGuy.getAuthToken()
                        }
                    }

                    jobs.forEach {
                        it.join() // wait for all coroutines to finish
                    }
                }.join()
            }
        }

        binding.suspendMutexDifferentThreads.setOnClickListener {
            lifecycleScope.launch {
                val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
                scope.launch {
                    val jobs: List<Job> = 1.rangeTo(100).map {
                        launch {
                            suspendMutexGuy.getAuthToken()
                        }
                    }

                    jobs.forEach {
                        it.join() // wait for all coroutines to finish
                    }
                }.join()
            }
        }

        binding.suspendMutexUiThread.setOnClickListener {
            lifecycleScope.launch {
                val jobs: List<Job> = 1.rangeTo(100).map {
                    launch {
                        suspendMutexGuy.getAuthToken()
                    }
                }

                jobs.forEach {
                    it.join() // wait for all coroutines to finish
                }
            }
        }

        binding.blockingMutexUiThread.setOnClickListener {
            lifecycleScope.launch {
                val jobs: List<Job> = 1.rangeTo(100).map {
                    launch {
                        blockingMutexGuy.getAuthToken()
                    }
                }

                jobs.forEach {
                    it.join() // wait for all coroutines to finish
                }
            }
        }

        binding.blockingMutexDifferentThreads.setOnClickListener {
            lifecycleScope.launch {
                val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
                scope.launch {
                    val jobs: List<Job> = 1.rangeTo(100).map {
                        launch {
                            blockingMutexGuy.getAuthToken()
                        }
                    }

                    jobs.forEach {
                        it.join() // wait for all coroutines to finish
                    }
                }.join()
            }
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun injectDependencies(context: Context) {
        (context as AppCompatActivity).application.getComponent<AppComponent>().inject(this)
    }
}
