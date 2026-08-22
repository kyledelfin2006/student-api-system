# AGENTS.md — Student CRUD App

## Purpose
Desktop Swing app for managing student records. Create, view, update, delete, and search students persisted to `Students.json`.

## Architecture
- **Pattern**: Layered (UI → Manager → Repository → JSON file)
- **Storage**: Jackson `ObjectMapper` serializes `List<Student>` to `Students.json`
- **Entry**: `Controller.Main` → `StudentManagementGUI`
- **Navigation**: `CardLayout` sidebar with 6 panels

## Package Structure
```
Controller/     — Main entry, wires GUI + manager
Manager/        — Business logic, orchestrates repository + validator
Repository/     — JSON persistence via Jackson
Model/          — Student POJO with field validation
UI/             — Swing panels (extends BasePanel)
  BasePanel     — Shared theme, fonts, colors, helper factories
Utils/          — Validator + ID generator
Exceptions/     — InvalidInputException (runtime)
```

## Core Features
| Panel | Actions |
|---|---|
| MainMenu | Dashboard shortcuts to all CRUD screens |
| CreateStudent | Form: firstName, lastName, email, GWA → manager.createStudent |
| ViewStudents | Table (editable cells except ID), sort by last name / GWA desc, refresh |
| UpdateStudent | Search by ID → display info → select field → new value → manager.updateStudentInfo |
| DeleteStudent | Search by ID → confirm dialog → manager.deleteStudent |
| AdvancedSearch | Search by partial name/email (case-insensitive), GWA min/max range → results table |

## Key Classes
- **Student** (`Model.Student`): id, firstName, lastName, email, gwa. All setters validate via `Validator`.
- **StudentRepository**: in-memory `List<Student>`, `loadFromFile()` / `saveToFile()` using Jackson `ObjectMapper` with pretty-print.
- **StudentManager**: CRUD operations + `searchStudents` for multi-field filtering. Constructor loads from file, computes max ID → seeds `StudentIDGenerator`.
- **StudentIDGenerator**: static zero-padded `%04d` auto-increment (0001, 0002…). `setNextId(maxId+1)` on load.
- **Validator**: throws `InvalidInputException`. Rules: name (letters/spaces/hyphens), email (regex), GWA (1.0–5.0 numeric), ID (0000–1000 numeric).
- **BasePanel**: abstract theme constants (Segoe UI, dark sidebar, blue primary). Factories: buttons, text fields, combo boxes, cards, forms.

## Data Flow
1. App starts → `StudentRepository("Students.json", new ArrayList<>())`
2. `StudentManager` loads JSON, parses max ID, sets generator
3. GUI panels receive `StudentManager` reference
4. CRUD actions call manager → validate → mutate list → `saveToFile()`
5. `ViewStudentsPanel.setValueAt` enables inline edits, directly calls `manager.repository.saveToFile()`
6. `AdvancedSearchPanel` collects optional filters (name/email partial, GWA range), calls `manager.searchStudents()`, displays results in a read-only table

## Constraints
- Java 21
- Dependency: `jackson-databind` 2.15.2 (no web framework, no test deps)
- JSON file is relative path, created on first save
- ID is immutable string, zero-padded 4 digits

## Implementation Notes
- Panels call `parentFrame.showPanel("Key")` to navigate
- `onPanelShown()` resets form state for each panel
- `ViewStudentsPanel` table row count matches `manager.getAllStudents()`; editing mutates the live list
- Validation occurs both in UI (pre-flight) and in `Student` setters (defense in depth)
