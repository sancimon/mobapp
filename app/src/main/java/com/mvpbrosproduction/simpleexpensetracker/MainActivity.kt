package com.mvpbrosproduction.simpleexpensetracker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mvpbrosproduction.simpleexpensetracker.composables.pages.AddExpensePage
import com.mvpbrosproduction.simpleexpensetracker.composables.pages.CategoriesSetupPage
import com.mvpbrosproduction.simpleexpensetracker.composables.pages.DailyExpenseLimitPage
import com.mvpbrosproduction.simpleexpensetracker.composables.pages.DailyExpensesPage
import com.mvpbrosproduction.simpleexpensetracker.composables.pages.DonationPage
import com.mvpbrosproduction.simpleexpensetracker.composables.pages.ExpenseInputSetupPage
import com.mvpbrosproduction.simpleexpensetracker.composables.pages.MonthlyExpensesPage
import com.mvpbrosproduction.simpleexpensetracker.composables.pages.MonthlyReportPage
import com.mvpbrosproduction.simpleexpensetracker.constants.DataStoreKeys
import com.mvpbrosproduction.simpleexpensetracker.constants.NavigationTarget
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseCategoryManager
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseManager
import com.mvpbrosproduction.simpleexpensetracker.models.NavigationItem
import com.mvpbrosproduction.simpleexpensetracker.repositories.CategoryRepository
import com.mvpbrosproduction.simpleexpensetracker.repositories.ExpenseRepository
import com.mvpbrosproduction.simpleexpensetracker.ui.theme.SimpleExpenseTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

private const val TAG = "MainActivity"

// DataStore
val Context.dataStore by preferencesDataStore(name = "variables")

class MainActivity : ComponentActivity() {

    private val _expenseRepository = ExpenseRepository(this)
    private val _categoryRepository = CategoryRepository(this)

    private var _categoryManager: ExpenseCategoryManager? = null
    private var _expenseManager: ExpenseManager? = null

    private val expenseLimitState: MutableDoubleState = mutableDoubleStateOf(0.0)

    private val _navigationItems: List<NavigationItem> = listOf(
        NavigationItem("Add Expense", Icons.Outlined.AttachMoney, NavigationTarget.ADD_EXPENSE),
        NavigationItem(
            "Daily Expenses",
            Icons.Outlined.Assignment,
            NavigationTarget.DAILY_EXPENSES
        ),
        NavigationItem(
            "Monthly Expenses",
            Icons.Outlined.CalendarMonth,
            NavigationTarget.MONTHLY_EXPENSES
        ),
        NavigationItem("Monthly Report", Icons.Outlined.Summarize, NavigationTarget.MONTHLY_REPORT),
        NavigationItem(
            "Set Daily Expense Cap",
            Icons.Outlined.Settings,
            NavigationTarget.DAILY_EXPENSES_LIMIT
        ),
        NavigationItem(
            "Categories Setup",
            Icons.Outlined.Category,
            NavigationTarget.CATEGORY_SETUP
        ),
        NavigationItem("About", Icons.Outlined.Info, NavigationTarget.INFO)
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navigationItemLabel = mutableStateOf(_navigationItems.first().label)

        _categoryManager = ExpenseCategoryManager(_categoryRepository)
        _expenseManager = ExpenseManager(_expenseRepository, expenseLimitState)

        val currentExpenseLimitFlow: Flow<Double> = dataStore.data.map { preferences ->
            preferences[DataStoreKeys.EXPENSE_LIMIT] ?: 0.0
        }

        // We observe the changes in the expense limit
        CoroutineScope(Dispatchers.Main).launch {
            currentExpenseLimitFlow.collect { expenseLimit ->
                // this state is passed to the composable
                expenseLimitState.doubleValue = expenseLimit

                _expenseManager?.reloadDailyAndMonthlyExpenses()
            }
        }

        setContent {
            val navController = rememberNavController()

            LaunchedEffect(navController) {
                // Add destination change listener
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    // Handle navigation destination change
                    val navigationItem: NavigationItem? =
                        _navigationItems.find { navigationItem -> navigationItem.target == destination.route }

                    if (navigationItem != null) {
                        navigationItemLabel.value = navigationItem.label
                    }
                }
            }

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            val toggleDrawer: () -> Unit = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }

            SimpleExpenseTrackerTheme {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.6f)) {
                            Text("Options", modifier = Modifier.padding(16.dp))
                            Divider()
                            _navigationItems.forEachIndexed { index, navigationItem ->
                                if (index == 4) {
                                    Divider()
                                }
                                NavigationDrawerItem(
                                    label = { Text(text = navigationItem.label) },
                                    icon = {
                                        Icon(navigationItem.icon, contentDescription = "home")
                                    },
                                    selected = false,
                                    onClick = {
                                        navController.navigate(navigationItem.target)

                                        toggleDrawer()
                                    },
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = {
                                    finishAffinity()
                                    exitProcess(0)
                                },
                                shape = RectangleShape,
                                modifier = Modifier.fillMaxWidth(1f)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.ExitToApp,
                                        contentDescription = "Exit App",
                                    )
                                    Text(
                                        text = "Exit",
                                    )
                                }
                            }

                        }
                    }
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        },
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(navigationItemLabel.value)
                                },
                                navigationIcon = {
                                    IconButton(onClick = { toggleDrawer() }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    ) { contentPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = NavigationTarget.ADD_EXPENSE,
                            modifier = Modifier.padding(contentPadding)
                        ) {
                            composable(NavigationTarget.ADD_EXPENSE) {
                                AddExpensePage(
                                    categoryManager = _categoryManager!!,
                                    expenseManager = _expenseManager!!,
                                    onExpenseAdded = { navController.navigate(NavigationTarget.DAILY_EXPENSES) }
                                )
                            }
                            composable(NavigationTarget.DAILY_EXPENSES) {
                                DailyExpensesPage(
                                    expenseManager = _expenseManager!!,
                                    categoryManager = _categoryManager!!,
                                    dailyExpenseLimit = expenseLimitState.doubleValue,
                                    onNextBtnClick = { navController.navigate(NavigationTarget.MONTHLY_EXPENSES) },
                                    onBackBtnClick = { navController.navigate(NavigationTarget.ADD_EXPENSE) }
                                )
                            }
                            composable(NavigationTarget.MONTHLY_EXPENSES) {
                                MonthlyExpensesPage(
                                    expenseManager = _expenseManager!!,
                                    snackbarHostState = snackbarHostState,
                                    navController = navController,
                                    onBackBtnClick = { navController.navigate(NavigationTarget.DAILY_EXPENSES) },
                                    onFwdBtnClick = { navController.navigate(NavigationTarget.MONTHLY_REPORT) }
                                )
                            }
                            composable(NavigationTarget.MONTHLY_REPORT) {
                                MonthlyReportPage(
                                    expenseManager = _expenseManager!!,
                                    dailyExpenseLimit = expenseLimitState.doubleValue,
                                    onBackBtnClick = {
                                        navController.navigate(NavigationTarget.MONTHLY_EXPENSES)
                                    },
                                )
                            }
                            composable(NavigationTarget.DAILY_EXPENSES_LIMIT) {
                                DailyExpenseLimitPage(dataStore)
                            }
                            composable(NavigationTarget.CATEGORY_SETUP) {
                                CategoriesSetupPage(categoryManager = _categoryManager!!)
                            }
                            composable(NavigationTarget.INFO) {
                                DonationPage()
                            }
                        }
                    }
                }
            }
        }
    }
}
