# Graph Report - VirtualClubs-FrontAndroid  (2026-07-16)

## Corpus Check
- 156 files · ~74,609 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 827 nodes · 1266 edges · 66 communities (47 shown, 19 thin omitted)
- Extraction: 89% EXTRACTED · 11% INFERRED · 0% AMBIGUOUS · INFERRED: 139 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `7a25987d`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Auth API & Repository
- Auth & Reset UI Screens
- Claude Commands & Docs
- Auth Interceptor & Network DI
- Settings UI Components
- Secure Token Storage
- Error Type Catalog
- Global Error UI Manager
- Home Screen & Club Model
- Splash & Token Refresh
- VC Button Component
- Session Manager & DI Entry Point
- Theme Elevation & Shape
- SafeResponse Tests
- App Preferences & DataStore DI
- User Model & Logout Tests
- GetUserInfo Use Case
- Auth Use Case
- Register Use Case
- Motion & Animation Theme
- UserSession Tests
- VirtualClubException & SafeCall Tests
- Google Sign-In Use Case
- SaveTokens Use Case
- Verify Email Activity
- AuthViewModel Tests
- App Preferences Impl
- Reset Password Activity
- Reset Password ViewModel
- Encryption Utils
- Session State & GetSessionState
- Screen Navigation Routes
- SafeCall Wrapper
- UserSession Core
- Verify Email Result ViewModel
- Test Dispatcher Rule
- Response Type DTO
- Typography Theme
- Gradlew Script
- Brand Logo Images
- App Entry Point (Hilt)
- Logout Use Case
- MainActivity
- Main Compose App Root
- Fake User Repository
- Instrumented Test Stub
- ApiResponse DTO
- Request Password Reset Use Case
- Gradient Theme
- API Endpoint Constants
- /explain Command
- /project-status Command
- CI Workflow
- SettingsPage
- FakeAuthRepository
- VCScaffold
- GetSessionStateUseCaseTest
- RoundedTextField
- SettingsViewModelTest
- AppBar.kt
- AppNavHost
- VerifyEmailResultPage

## God Nodes (most connected - your core abstractions)
1. `ErrorType` - 35 edges
2. `VirtualClubException` - 33 edges
3. `User` - 27 edges
4. `UserSession` - 26 edges
5. `FakeAuthRepository` - 23 edges
6. `GlobalUIManager` - 21 edges
7. `AuthViewModelTest` - 19 edges
8. `VCButton()` - 18 edges
9. `AppPreferences` - 17 edges
10. `AuthTokens` - 17 edges

## Surprising Connections (you probably didn't know these)
- `GitHub Pull Request Template` --semantically_similar_to--> `/check-structure command`  [INFERRED] [semantically similar]
  .github/pull_request_template.md → .claude/commands/check-structure.md
- `/check-ds command` --conceptually_related_to--> `Stadium Design System`  [INFERRED]
  .claude/commands/check-ds.md → README.md
- `Fake repositories over mocks in tests` --rationale_for--> `/add-test command`  [INFERRED]
  README.md → .claude/commands/add-test.md
- `/do-task command` --shares_data_with--> `Project CLAUDE.md`  [INFERRED]
  .claude/commands/do-task.md → CLAUDE.md
- `VirtualClubsMainApp()` --calls--> `AppNavHost()`  [INFERRED]
  app/src/main/java/es/virtualclubs/VirtualClubsMainApp.kt → app/src/main/java/es/virtualclubs/presentation/navigation/NavGraph.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Plane work-item lifecycle automation across slash commands** — claude_commands_do_task_dotask, claude_commands_new_feature_newfeature, claude_commands_release_debug_releasedebug, concept_plane [INFERRED 0.80]
- **Commit, PR creation, PR template, and PR review flow** — claude_commands_commit_commit, claude_commands_create_pr_createpr, claude_commands_review_pr_reviewpr, github_pull_request_template_pullrequesttemplate [EXTRACTED 0.90]
- **Design system token enforcement across theme files and /check-ds** — claude_commands_check_ds_checkds, presentation_theme_color_color, presentation_theme_type_type, presentation_theme_shape_shape, presentation_theme_theme_theme, concept_stadiumdesignsystem [EXTRACTED 0.90]

## Communities (66 total, 19 thin omitted)

### Community 0 - "Auth API & Repository"
Cohesion: 0.07
Nodes (16): AuthApi, RefreshApi, ApiResponse, getOrThrow(), T, AuthRequest, GoogleAuthRequest, RefreshRequest (+8 more)

### Community 1 - "Auth & Reset UI Screens"
Cohesion: 0.17
Nodes (11): Activity, Attempting, AttemptingAuth, AuthUiState, AuthViewModel, Idle, StateFlow, ViewModel (+3 more)

### Community 2 - "Claude Commands & Docs"
Cohesion: 0.07
Nodes (44): Project CLAUDE.md, /add-api-endpoint command, /add-error-type command, /add-screen command, /add-test command, /add-usecase command, /check-ds command, /check-security command (+36 more)

### Community 3 - "Auth Interceptor & Network DI"
Cohesion: 0.06
Nodes (19): AuthInterceptor, Result, T, SafeResponse, UserApi, UserInfoDto, Result, RefreshRepositoryImpl (+11 more)

### Community 4 - "Settings UI Components"
Cohesion: 0.15
Nodes (8): AppearanceSection(), AppearanceSectionPreview(), Modifier, StateFlow, ViewModel, PrefsSnapshot, SettingsUiState, SettingsViewModel

### Community 5 - "Secure Token Storage"
Cohesion: 0.05
Nodes (11): Context, Flow, SecureUserPreferences, ClearTokensUseCase, GetAccessTokenUseCase, GetRefreshTokenUseCase, SaveTokensUseCase, ClearTokensUseCaseTest (+3 more)

### Community 6 - "Error Type Catalog"
Cohesion: 0.06
Nodes (30): ErrorType, CANT_CONNECT_SERVER, EMAIL_ALREADY_EXISTS, EMAIL_NOT_FOUND, EMAIL_NOT_VERIFIED, EMAIL_REQUIRED, FAILED_SEND_EMAIL, FIELD_BLANK (+22 more)

### Community 7 - "Global Error UI Manager"
Cohesion: 0.09
Nodes (9): DispatcherModule, ErrorDispatcher, EmailNotVerifiedDialog, VCDialog, DialogState, ErrorUiState, GlobalUIManager, StateFlow (+1 more)

### Community 8 - "Home Screen & Club Model"
Cohesion: 0.21
Nodes (12): Club, ClubCard(), HomeActions(), HomeContent(), HomeContentWithClubsPreview(), HomeEmptyClubs(), HomeHeader(), HomePage() (+4 more)

### Community 9 - "Splash & Token Refresh"
Cohesion: 0.09
Nodes (11): RefreshTokenUseCase, Home, StateFlow, ViewModel, Login, SplashDestination, SplashViewModel, RefreshTokenUseCaseTest (+3 more)

### Community 10 - "VC Button Component"
Cohesion: 0.15
Nodes (24): ButtonContent(), Drawable, Icon, Color, Modifier, Text, TextAndIcon, VCButton() (+16 more)

### Community 11 - "Session Manager & DI Entry Point"
Cohesion: 0.11
Nodes (5): GlobalUIEntryPoint, SessionManager, AppNavigator, NavController, SessionManagerTest

### Community 12 - "Theme Elevation & Shape"
Cohesion: 0.16
Nodes (13): Bundle, VCElevation, VCShapes, Sizes, Spacing, getColorScheme(), getLargeLogo(), getLogo() (+5 more)

### Community 13 - "SafeResponse Tests"
Cohesion: 0.26
Nodes (3): Result, SafeResponseTest, HttpException

### Community 14 - "App Preferences & DataStore DI"
Cohesion: 0.21
Nodes (6): Flow, UserPreferences, Context, PreferencesModule, DataStore, Preferences

### Community 15 - "User Model & Logout Tests"
Cohesion: 0.13
Nodes (3): LogoutUserUseCase, LogoutUserUseCaseTest, Exception

### Community 16 - "GetUserInfo Use Case"
Cohesion: 0.18
Nodes (4): User, GetUserInfoUseCase, Result, GetUserInfoUseCaseTest

### Community 19 - "Motion & Animation Theme"
Cohesion: 0.17
Nodes (4): VCDuration, VCEasing, VCSpring, VCTween

### Community 23 - "SaveTokens Use Case"
Cohesion: 0.18
Nodes (10): ImageVector, Modifier, VCListItem(), VCListToggleItem(), ImageVector, Modifier, SettingRow(), ImageVector (+2 more)

### Community 24 - "Verify Email Activity"
Cohesion: 0.27
Nodes (6): Bundle, ComponentActivity, Intent, NavController, NavHostController, VerifyEmailActivity

### Community 27 - "Reset Password Activity"
Cohesion: 0.33
Nodes (5): Bundle, ComponentActivity, Intent, NavHostController, ResetPasswordActivity

### Community 28 - "Reset Password ViewModel"
Cohesion: 0.19
Nodes (8): Attempting, Idle, StateFlow, ViewModel, ResetPasswordUiState, ResetPasswordViewModel, Success, ResetPasswordViewModelTest

### Community 29 - "Encryption Utils"
Cohesion: 0.36
Nodes (3): EncryptionUtils, ByteArray, SecretKey

### Community 30 - "Session State & GetSessionState"
Cohesion: 0.47
Nodes (4): LoggedIn, LoggedOut, SessionState, Flow

### Community 31 - "Screen Navigation Routes"
Cohesion: 0.46
Nodes (7): Auth, Home, ResetPassword, Screen, Settings, Splash, VerifyEmailResult

### Community 32 - "SafeCall Wrapper"
Cohesion: 0.25
Nodes (3): Result, T, SafeCall

### Community 34 - "Verify Email Result ViewModel"
Cohesion: 0.24
Nodes (7): Error, StateFlow, ViewModel, Success, VerifyEmailResultUiState, VerifyEmailResultViewModel, VerifyEmailResultViewModelTest

### Community 35 - "Test Dispatcher Rule"
Cohesion: 0.40
Nodes (3): MainDispatcherRule, Description, TestWatcher

### Community 36 - "Response Type DTO"
Cohesion: 0.50
Nodes (4): from(), ResponseType, ERROR, NONE

### Community 37 - "Typography Theme"
Cohesion: 0.40
Nodes (4): Typography, resolveTypography(), Typography, scaledTypography()

### Community 38 - "Gradlew Script"
Cohesion: 0.60
Nodes (3): gradlew script, die(), warn()

### Community 39 - "Brand Logo Images"
Cohesion: 0.67
Nodes (4): Play Store App Icon (Crossed Dumbbells), VirtualClubs Wordmark - Light Text Variant (logo_text_black.png), VirtualClubs Wordmark - Dark Blue Text Variant (logo_text_white.png), VirtualClubs Icon-Only Mark (logo_whitout_text.png)

### Community 41 - "Logout Use Case"
Cohesion: 0.24
Nodes (9): Modifier, meetsAllPasswordRequirements(), PasswordRequirement, PasswordRequirements(), ImeAction, Modifier, ResetPasswordField(), ResetPasswordIcon() (+1 more)

### Community 42 - "MainActivity"
Cohesion: 0.50
Nodes (3): ComponentActivity, NavHostController, MainActivity

### Community 43 - "Main Compose App Root"
Cohesion: 0.50
Nodes (3): NavHostController, VirtualClubsMainApp(), WindowWidthSizeClass

### Community 44 - "Fake User Repository"
Cohesion: 0.27
Nodes (3): FakeUserRepository, Result, HomeViewModelTest

### Community 46 - "ApiResponse DTO"
Cohesion: 0.24
Nodes (8): Modifier, LoginContent(), LoginPage(), AuthDivider(), Color, Modifier, SocialButton(), SocialIconButton()

### Community 57 - "SettingsPage"
Cohesion: 0.22
Nodes (7): DebugServerSection(), LanguagePickerDialog, AccountCard(), SectionHeader(), SettingsPage(), getAppVersion(), Context

### Community 59 - "VCScaffold"
Cohesion: 0.22
Nodes (7): Color, Modifier, RowScope, VCScaffold(), SplashPage(), Composable, WindowInsets

### Community 61 - "RoundedTextField"
Cohesion: 0.25
Nodes (6): ImageVector, ImeAction, Modifier, RoundedTextField(), ForgotPasswordDialog(), KeyboardType

### Community 63 - "AppBar.kt"
Cohesion: 0.38
Nodes (6): AppBar(), Error, Modifier, RowScope, None, UiMessage

### Community 64 - "AppNavHost"
Cohesion: 0.29
Nodes (5): AppNavHost(), NavHostController, ScreenType, Medium, Small

### Community 65 - "VerifyEmailResultPage"
Cohesion: 0.47
Nodes (5): Color, ImageVector, VerifyEmailResultContent(), VerifyEmailResultErrorPreview(), VerifyEmailResultPage()

## Ambiguous Edges - Review These
- `Play Store App Icon (Crossed Dumbbells)` → `VirtualClubs Icon-Only Mark (logo_whitout_text.png)`  [AMBIGUOUS]
  app/src/main/ic_launcher-playstore.png · relation: conceptually_related_to

## Knowledge Gaps
- **55 isolated node(s):** `Small`, `Medium`, `Endpoint`, `NONE`, `ERROR` (+50 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **19 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **What is the exact relationship between `Play Store App Icon (Crossed Dumbbells)` and `VirtualClubs Icon-Only Mark (logo_whitout_text.png)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **Why does `UserSession` connect `UserSession Core` to `SafeCall Wrapper`, `Auth Interceptor & Network DI`, `Secure Token Storage`, `Splash & Token Refresh`, `Fake User Repository`, `User Model & Logout Tests`, `GetUserInfo Use Case`, `UserSession Tests`, `SettingsViewModelTest`, `AuthViewModel Tests`, `GetSessionStateUseCaseTest`, `Session State & GetSessionState`?**
  _High betweenness centrality (0.172) - this node is a cross-community bridge._
- **Why does `VirtualClubException` connect `VirtualClubException & SafeCall Tests` to `SafeCall Wrapper`, `Auth API & Repository`, `Auth Interceptor & Network DI`, `Secure Token Storage`, `Splash & Token Refresh`, `Fake User Repository`, `SafeResponse Tests`, `User Model & Logout Tests`, `GetUserInfo Use Case`, `Auth Use Case`, `Register Use Case`, `Request Password Reset Use Case`, `Google Sign-In Use Case`, `AuthViewModel Tests`?**
  _High betweenness centrality (0.101) - this node is a cross-community bridge._
- **Why does `AppPreferences` connect `App Preferences Impl` to `Auth Interceptor & Network DI`, `Typography Theme`, `MainActivity`, `Theme Elevation & Shape`, `App Preferences & DataStore DI`, `Verify Email Activity`, `Reset Password Activity`, `SettingsViewModelTest`?**
  _High betweenness centrality (0.098) - this node is a cross-community bridge._
- **Are the 31 inferred relationships involving `VirtualClubException` (e.g. with `.saveAccessToken()` and `.saveRefreshToken()`) actually correct?**
  _`VirtualClubException` has 31 INFERRED edges - model-reasoned connections that need verification._
- **Are the 20 inferred relationships involving `User` (e.g. with `.loginUser()` and `.processGoogleCredential()`) actually correct?**
  _`User` has 20 INFERRED edges - model-reasoned connections that need verification._
- **What connects `Small`, `Medium`, `Endpoint` to the rest of the system?**
  _55 weakly-connected nodes found - possible documentation gaps or missing edges._