# Graph Report - .  (2026-07-15)

## Corpus Check
- 170 files · ~73,337 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 786 nodes · 1179 edges · 57 communities (37 shown, 20 thin omitted)
- Extraction: 90% EXTRACTED · 10% INFERRED · 0% AMBIGUOUS · INFERRED: 122 edges (avg confidence: 0.8)
- Token cost: 167,652 input · 0 output

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

## God Nodes (most connected - your core abstractions)
1. `ErrorType` - 35 edges
2. `VirtualClubException` - 31 edges
3. `UserSession` - 20 edges
4. `User` - 20 edges
5. `GlobalUIManager` - 19 edges
6. `FakeAuthRepository` - 19 edges
7. `AuthViewModelTest` - 19 edges
8. `VCButton()` - 18 edges
9. `AuthTokens` - 17 edges
10. `LoginContent()` - 17 edges

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

## Communities (57 total, 20 thin omitted)

### Community 0 - "Auth API & Repository"
Cohesion: 0.06
Nodes (16): AuthApi, RefreshApi, ApiResponse, AuthRequest, GoogleAuthRequest, RefreshRequest, RegisterRequest, RequestPasswordResetRequest (+8 more)

### Community 1 - "Auth & Reset UI Screens"
Cohesion: 0.05
Nodes (39): Activity, Modifier, meetsAllPasswordRequirements(), PasswordRequirement, PasswordRequirements(), ImageVector, ImeAction, Modifier (+31 more)

### Community 2 - "Claude Commands & Docs"
Cohesion: 0.07
Nodes (44): Project CLAUDE.md, /add-api-endpoint command, /add-error-type command, /add-screen command, /add-test command, /add-usecase command, /check-ds command, /check-security command (+36 more)

### Community 3 - "Auth Interceptor & Network DI"
Cohesion: 0.06
Nodes (19): AuthInterceptor, Result, T, SafeResponse, UserApi, UserInfoDto, Result, RefreshRepositoryImpl (+11 more)

### Community 4 - "Settings UI Components"
Cohesion: 0.06
Nodes (25): ImageVector, Modifier, VCListItem(), VCListToggleItem(), AppearanceSection(), AppearanceSectionPreview(), Modifier, DebugServerSection() (+17 more)

### Community 5 - "Secure Token Storage"
Cohesion: 0.07
Nodes (9): Context, Flow, SecureUserPreferences, ClearTokensUseCase, GetAccessTokenUseCase, GetRefreshTokenUseCase, ClearTokensUseCaseTest, GetAccessTokenUseCaseTest (+1 more)

### Community 6 - "Error Type Catalog"
Cohesion: 0.06
Nodes (30): ErrorType, CANT_CONNECT_SERVER, EMAIL_ALREADY_EXISTS, EMAIL_NOT_FOUND, EMAIL_NOT_VERIFIED, EMAIL_REQUIRED, FAILED_SEND_EMAIL, FIELD_BLANK (+22 more)

### Community 7 - "Global Error UI Manager"
Cohesion: 0.09
Nodes (9): DispatcherModule, ErrorDispatcher, EmailNotVerifiedDialog, VCDialog, DialogState, ErrorUiState, GlobalUIManager, StateFlow (+1 more)

### Community 8 - "Home Screen & Club Model"
Cohesion: 0.09
Nodes (25): Club, AppBar(), Error, Modifier, RowScope, None, UiMessage, Color (+17 more)

### Community 9 - "Splash & Token Refresh"
Cohesion: 0.09
Nodes (11): RefreshTokenUseCase, Home, StateFlow, ViewModel, Login, SplashDestination, SplashViewModel, RefreshTokenUseCaseTest (+3 more)

### Community 10 - "VC Button Component"
Cohesion: 0.12
Nodes (29): ButtonContent(), Drawable, Icon, Color, Modifier, Text, TextAndIcon, VCButton() (+21 more)

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
Cohesion: 0.16
Nodes (3): User, LogoutUserUseCaseTest, Exception

### Community 16 - "GetUserInfo Use Case"
Cohesion: 0.17
Nodes (3): GetUserInfoUseCase, Result, GetUserInfoUseCaseTest

### Community 19 - "Motion & Animation Theme"
Cohesion: 0.17
Nodes (4): VCDuration, VCEasing, VCSpring, VCTween

### Community 24 - "Verify Email Activity"
Cohesion: 0.27
Nodes (6): Bundle, ComponentActivity, Intent, NavController, NavHostController, VerifyEmailActivity

### Community 27 - "Reset Password Activity"
Cohesion: 0.33
Nodes (5): Bundle, ComponentActivity, Intent, NavHostController, ResetPasswordActivity

### Community 28 - "Reset Password ViewModel"
Cohesion: 0.33
Nodes (7): Attempting, Idle, StateFlow, ViewModel, ResetPasswordUiState, ResetPasswordViewModel, Success

### Community 29 - "Encryption Utils"
Cohesion: 0.36
Nodes (3): EncryptionUtils, ByteArray, SecretKey

### Community 30 - "Session State & GetSessionState"
Cohesion: 0.32
Nodes (5): LoggedIn, LoggedOut, SessionState, GetSessionStateUseCase, Flow

### Community 31 - "Screen Navigation Routes"
Cohesion: 0.46
Nodes (7): Auth, Home, ResetPassword, Screen, Settings, Splash, VerifyEmailResult

### Community 32 - "SafeCall Wrapper"
Cohesion: 0.29
Nodes (3): Result, T, SafeCall

### Community 34 - "Verify Email Result ViewModel"
Cohesion: 0.43
Nodes (6): Error, StateFlow, ViewModel, Success, VerifyEmailResultUiState, VerifyEmailResultViewModel

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

### Community 42 - "MainActivity"
Cohesion: 0.50
Nodes (3): ComponentActivity, NavHostController, MainActivity

### Community 43 - "Main Compose App Root"
Cohesion: 0.50
Nodes (3): NavHostController, VirtualClubsMainApp(), WindowWidthSizeClass

## Ambiguous Edges - Review These
- `Play Store App Icon (Crossed Dumbbells)` → `VirtualClubs Icon-Only Mark (logo_whitout_text.png)`  [AMBIGUOUS]
  app/src/main/ic_launcher-playstore.png · relation: conceptually_related_to

## Knowledge Gaps
- **55 isolated node(s):** `Small`, `Medium`, `Endpoint`, `NONE`, `ERROR` (+50 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **20 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **What is the exact relationship between `Play Store App Icon (Crossed Dumbbells)` and `VirtualClubs Icon-Only Mark (logo_whitout_text.png)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **Why does `UserSession` connect `UserSession Core` to `SafeCall Wrapper`, `Auth Interceptor & Network DI`, `Logout Use Case`, `Splash & Token Refresh`, `User Model & Logout Tests`, `GetUserInfo Use Case`, `UserSession Tests`, `SaveTokens Use Case`, `AuthViewModel Tests`, `Session State & GetSessionState`?**
  _High betweenness centrality (0.151) - this node is a cross-community bridge._
- **Why does `AuthViewModel` connect `Auth & Reset UI Screens` to `AuthViewModel Tests`?**
  _High betweenness centrality (0.138) - this node is a cross-community bridge._
- **Why does `AuthViewModelTest` connect `AuthViewModel Tests` to `SafeCall Wrapper`, `UserSession Core`, `Global Error UI Manager`, `App Preferences & DataStore DI`, `Request Password Reset Use Case`, `Auth Use Case`, `Register Use Case`, `Google Sign-In Use Case`, `SaveTokens Use Case`?**
  _High betweenness centrality (0.122) - this node is a cross-community bridge._
- **Are the 29 inferred relationships involving `VirtualClubException` (e.g. with `.saveAccessToken()` and `.saveRefreshToken()`) actually correct?**
  _`VirtualClubException` has 29 INFERRED edges - model-reasoned connections that need verification._
- **Are the 13 inferred relationships involving `User` (e.g. with `.loginUser()` and `.processGoogleCredential()`) actually correct?**
  _`User` has 13 INFERRED edges - model-reasoned connections that need verification._
- **What connects `Small`, `Medium`, `Endpoint` to the rest of the system?**
  _55 weakly-connected nodes found - possible documentation gaps or missing edges._