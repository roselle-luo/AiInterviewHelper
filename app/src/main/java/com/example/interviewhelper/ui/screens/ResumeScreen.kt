package com.example.interviewhelper.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.interviewhelper.data.model.Education
import com.example.interviewhelper.data.model.PersonalInfo
import com.example.interviewhelper.data.model.Project
import com.example.interviewhelper.data.model.WorkExperience
import com.example.interviewhelper.viewmodel.ResumeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeEditScreen(
    navController: NavController,
    viewModel: ResumeViewModel = hiltViewModel()
) {
    val resumeData by viewModel.resumeData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf(
        TabItem("个人信息", Icons.Default.Person),
        TabItem("工作经历", Icons.Default.Email),
        TabItem("教育背景", Icons.Default.Home),
        TabItem("技能特长", Icons.Default.Build),
        TabItem("项目经验", Icons.Default.DateRange)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("简历管理") },
                actions = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        IconButton(onClick = { viewModel.saveResume() }) {
                            Icon(Icons.Default.Info, contentDescription = "保存")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab栏
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(tab.title, fontSize = 11.5.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        icon = { Icon(tab.icon, contentDescription = tab.title) }
                    )
                }
            }

            // 内容区域
            when (selectedTabIndex) {
                0 -> PersonalInfoSection(
                    personalInfo = resumeData.personalInfo,
                    onPersonalInfoChange = viewModel::updatePersonalInfo
                )
                1 -> WorkExperienceSection(
                    workExperiences = resumeData.workExperiences,
                    onAddExperience = viewModel::addWorkExperience,
                    onUpdateExperience = viewModel::updateWorkExperience,
                    onRemoveExperience = viewModel::removeWorkExperience
                )
                2 -> EducationSection(
                    educations = resumeData.educations,
                    onAddEducation = viewModel::addEducation,
                    onUpdateEducation = viewModel::updateEducation,
                    onRemoveEducation = viewModel::removeEducation
                )
                3 -> SkillsSection(
                    skills = resumeData.skills,
                    onAddSkill = viewModel::addSkill,
                    onRemoveSkill = viewModel::removeSkill
                )
                4 -> ProjectsSection(
                    projects = resumeData.projects,
                    onAddProject = viewModel::addProject,
                    onUpdateProject = viewModel::updateProject,
                    onRemoveProject = viewModel::removeProject
                )
            }
        }
    }
}

data class TabItem(
    val title: String,
    val icon: ImageVector
)

// PersonalInfoSection.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoSection(
    personalInfo: PersonalInfo,
    onPersonalInfoChange: (PersonalInfo) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "基本信息",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = personalInfo.name,
                        onValueChange = { onPersonalInfoChange(personalInfo.copy(name = it)) },
                        label = { Text("姓名") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = personalInfo.email,
                        onValueChange = { onPersonalInfoChange(personalInfo.copy(email = it)) },
                        label = { Text("邮箱") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = personalInfo.phone,
                        onValueChange = { onPersonalInfoChange(personalInfo.copy(phone = it)) },
                        label = { Text("电话") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = personalInfo.address,
                        onValueChange = { onPersonalInfoChange(personalInfo.copy(address = it)) },
                        label = { Text("地址") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = personalInfo.summary,
                        onValueChange = { onPersonalInfoChange(personalInfo.copy(summary = it)) },
                        label = { Text("个人简介") },
                        leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }
        }
    }
}


// WorkExperienceSection.kt (续)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkExperienceSection(
    workExperiences: List<WorkExperience>,
    onAddExperience: (WorkExperience) -> Unit,
    onUpdateExperience: (WorkExperience) -> Unit,
    onRemoveExperience: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingExperience by remember { mutableStateOf<WorkExperience?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "工作经历",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "添加工作经历")
                }
            }
        }

        items(workExperiences) { experience ->
            WorkExperienceCard(
                experience = experience,
                onEdit = { editingExperience = experience },
                onDelete = { onRemoveExperience(experience.id) }
            )
        }
    }

    // 添加/编辑对话框
    if (showAddDialog || editingExperience != null) {
        WorkExperienceDialog(
            experience = editingExperience,
            onDismiss = {
                showAddDialog = false
                editingExperience = null
            },
            onSave = { experience ->
                if (editingExperience != null) {
                    onUpdateExperience(experience)
                } else {
                    onAddExperience(experience)
                }
                showAddDialog = false
                editingExperience = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkExperienceCard(
    experience: WorkExperience,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = experience.position,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = experience.company,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${experience.startDate} - ${if (experience.isCurrentJob) "至今" else experience.endDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "编辑")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "删除")
                    }
                }
            }

            if (experience.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = experience.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkExperienceDialog(
    experience: WorkExperience?,
    onDismiss: () -> Unit,
    onSave: (WorkExperience) -> Unit
) {
    var company by remember { mutableStateOf(experience?.company ?: "") }
    var position by remember { mutableStateOf(experience?.position ?: "") }
    var startDate by remember { mutableStateOf(experience?.startDate ?: "") }
    var endDate by remember { mutableStateOf(experience?.endDate ?: "") }
    var description by remember { mutableStateOf(experience?.description ?: "") }
    var isCurrentJob by remember { mutableStateOf(experience?.isCurrentJob ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (experience != null) "编辑工作经历" else "添加工作经历") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = company,
                        onValueChange = { company = it },
                        label = { Text("公司名称") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = position,
                        onValueChange = { position = it },
                        label = { Text("职位") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("开始时间") },
                        placeholder = { Text("如：2023-01") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isCurrentJob,
                            onCheckedChange = { isCurrentJob = it }
                        )
                        Text("目前仍在职")
                    }
                }
                if (!isCurrentJob) {
                    item {
                        OutlinedTextField(
                            value = endDate,
                            onValueChange = { endDate = it },
                            label = { Text("结束时间") },
                            placeholder = { Text("如：2024-01") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("工作描述") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newExperience = WorkExperience(
                        id = experience?.id ?: "",
                        company = company,
                        position = position,
                        startDate = startDate,
                        endDate = if (isCurrentJob) "" else endDate,
                        description = description,
                        isCurrentJob = isCurrentJob
                    )
                    onSave(newExperience)
                }
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}


// EducationSection.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationSection(
    educations: List<Education>,
    onAddEducation: (Education) -> Unit,
    onUpdateEducation: (Education) -> Unit,
    onRemoveEducation: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingEducation by remember { mutableStateOf<Education?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "教育背景",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "添加教育经历")
                }
            }
        }

        items(educations) { education ->
            EducationCard(
                education = education,
                onEdit = { editingEducation = education },
                onDelete = { onRemoveEducation(education.id) }
            )
        }
    }

    if (showAddDialog || editingEducation != null) {
        EducationDialog(
            education = editingEducation,
            onDismiss = {
                showAddDialog = false
                editingEducation = null
            },
            onSave = { education ->
                if (editingEducation != null) {
                    onUpdateEducation(education)
                } else {
                    onAddEducation(education)
                }
                showAddDialog = false
                editingEducation = null
            }
        )
    }
}

@Composable
fun EducationCard(
    education: Education,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = education.school,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${education.degree} - ${education.major}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${education.startDate} - ${education.endDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (education.gpa.isNotBlank()) {
                        Text(
                            text = "GPA: ${education.gpa}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "编辑")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "删除")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationDialog(
    education: Education?,
    onDismiss: () -> Unit,
    onSave: (Education) -> Unit
) {
    var school by remember { mutableStateOf(education?.school ?: "") }
    var degree by remember { mutableStateOf(education?.degree ?: "") }
    var major by remember { mutableStateOf(education?.major ?: "") }
    var startDate by remember { mutableStateOf(education?.startDate ?: "") }
    var endDate by remember { mutableStateOf(education?.endDate ?: "") }
    var gpa by remember { mutableStateOf(education?.gpa ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (education != null) "编辑教育经历" else "添加教育经历") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = school,
                        onValueChange = { school = it },
                        label = { Text("学校名称") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = degree,
                        onValueChange = { degree = it },
                        label = { Text("学位") },
                        placeholder = { Text("如：本科、硕士、博士") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = major,
                        onValueChange = { major = it },
                        label = { Text("专业") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("入学时间") },
                        placeholder = { Text("如：2020-09") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("毕业时间") },
                        placeholder = { Text("如：2024-06") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = gpa,
                        onValueChange = { gpa = it },
                        label = { Text("GPA (可选)") },
                        placeholder = { Text("如：3.8/4.0") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newEducation = Education(
                        id = education?.id ?: "",
                        school = school,
                        degree = degree,
                        major = major,
                        startDate = startDate,
                        endDate = endDate,
                        gpa = gpa
                    )
                    onSave(newEducation)
                }
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsSection(
    skills: List<String>,
    onAddSkill: (String) -> Unit,
    onRemoveSkill: (String) -> Unit
) {
    var newSkill by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "技能特长",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "添加技能",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newSkill,
                            onValueChange = { newSkill = it },
                            label = { Text("技能名称") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newSkill.isNotBlank()) {
                                    onAddSkill(newSkill.trim())
                                    newSkill = ""
                                }
                            },
                            enabled = newSkill.isNotBlank()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("添加")
                        }
                    }
                }
            }
        }

        if (skills.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "已添加的技能",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // 使用FlowRow替代LazyVerticalGrid来避免嵌套滚动问题
                        SkillsFlowLayout(
                            skills = skills,
                            onRemoveSkill = onRemoveSkill
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsFlowLayout(
    skills: List<String>,
    onRemoveSkill: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        skills.forEach { skill ->
            SkillChip(
                skill = skill,
                onRemove = { onRemoveSkill(skill) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillChip(
    skill: String,
    onRemove: () -> Unit
) {
    InputChip(
        onClick = { },
        label = { Text(skill) },
        selected = false,
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = "删除",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onRemove() }
            )
        }
    )
}


// ProjectsSection.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsSection(
    projects: List<Project>,
    onAddProject: (Project) -> Unit,
    onUpdateProject: (Project) -> Unit,
    onRemoveProject: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingProject by remember { mutableStateOf<Project?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "项目经验",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "添加项目")
                }
            }
        }

        items(projects) { project ->
            ProjectCard(
                project = project,
                onEdit = { editingProject = project },
                onDelete = { onRemoveProject(project.id) }
            )
        }
    }

    if (showAddDialog || editingProject != null) {
        ProjectDialog(
            project = editingProject,
            onDismiss = {
                showAddDialog = false
                editingProject = null
            },
            onSave = { project ->
                if (editingProject != null) {
                    onUpdateProject(project)
                } else {
                    onAddProject(project)
                }
                showAddDialog = false
                editingProject = null
            }
        )
    }
}

@Composable
fun ProjectCard(
    project: Project,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (project.technologies.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "技术栈: ${project.technologies}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (project.url.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = project.url,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "编辑")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "删除")
                    }
                }
            }

            if (project.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDialog(
    project: Project?,
    onDismiss: () -> Unit,
    onSave: (Project) -> Unit
) {
    var name by remember { mutableStateOf(project?.name ?: "") }
    var description by remember { mutableStateOf(project?.description ?: "") }
    var technologies by remember { mutableStateOf(project?.technologies ?: "") }
    var url by remember { mutableStateOf(project?.url ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (project != null) "编辑项目" else "添加项目") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("项目名称") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("项目描述") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
                item {
                    OutlinedTextField(
                        value = technologies,
                        onValueChange = { technologies = it },
                        label = { Text("技术栈") },
                        placeholder = { Text("如：Kotlin, Jetpack Compose, MVVM") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = url,
                        onValueChange = { url = it },
                        label = { Text("项目链接 (可选)") },
                        placeholder = { Text("如：GitHub链接或演示地址") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newProject = Project(
                        id = project?.id ?: "",
                        name = name,
                        description = description,
                        technologies = technologies,
                        url = url
                    )
                    onSave(newProject)
                }
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}