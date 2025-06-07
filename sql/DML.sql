-- Inserción de datos en la tabla categories
INSERT INTO categories (id, name, description)
VALUES
    ('1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed', 'Salud', 'Hábitos y artículos relacionados con la salud física y mental'),
    ('6ecd8c99-4036-403d-bf84-cf8400f67836', 'Productividad', 'Técnicas y rutinas para mejorar la eficiencia personal'),
    ('3e7c78bc-e145-4c2d-aa36-58b97f68a6d3', 'Deporte', 'Actividades físicas y entrenamiento'),
    ('b5a9f41d-31a6-4f11-b80f-7d2764c0a75b', 'Educación', 'Aprendizaje y desarrollo de habilidades intelectuales'),
    ('d2546fcd-39d8-4370-b34c-87d472a7a757', 'Alimentación', 'Nutrición y dietas saludables'),
    ('7d7cfd2a-86a3-4879-aa4f-2e5413b3538b', 'Meditación', 'Prácticas de mindfulness y bienestar emocional'),
    ('98f1c86b-8fec-4d71-b95e-da7f2e4c0ff3', 'Finanzas', 'Hábitos de ahorro y gestión económica');

-- Inserción de datos en la tabla week_days
INSERT INTO week_days (id, name, abbreviation)
VALUES
    ('d3b15c58-711f-40d9-9f4b-06d0d6e925d1', 'Lunes', 'LU'),
    ('b9b3995e-c6a5-46c7-bf8a-f1c1c2e65dd6', 'Martes', 'MA'),
    ('4afe91c2-9851-4af7-b282-39a543989ea3', 'Miércoles', 'MI'),
    ('ea5e7c7a-182c-4b49-8b2d-2162cd138384', 'Jueves', 'JU'),
    ('22f2bf21-fcbd-473f-98d2-96ba47fabe16', 'Viernes', 'VI'),
    ('f31a5698-2a4d-4818-8a0b-e7f843b9ec14', 'Sábado', 'SA'),
    ('82a4b1c9-72a8-4e91-aaa4-2c92d30b810f', 'Domingo', 'DO');

-- Script de migración para versión anterior de Directus (sin tabla directus_policies)

-- Primero mantenemos la inserción de roles que sigue siendo compatible
INSERT INTO directus_roles (id, name)
VALUES
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'ADMIN'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'USER');

-- Inserción de usuarios (compatible con ambas versiones)
INSERT INTO directus_users (id, email, password, role)
VALUES
    ('05e8c0f7-6433-4e23-ab06-36a67c7e23a3', 'admin@ejemplo.com', '$argon2id$v=19$m=64,t=3,p=4$bjJxemdpaTZhamYwMDAwMA$mQp7+d3cVuwTx71lb6TrqiZU/lB3Ph8l4K5okmS5f9oeWPzk/lzR6jAJ2ozWuxhIGSQ6iy3O8tnLRB7QcZ0O0g', 'a21cfc5d-3f01-4e45-8e93-dd0d440af562'),
    ('8c7e5a5d-6eb8-4f21-b11c-2f7c31b38d15', 'juan_colonia@ejemplo.com', '$argon2id$v=19$m=64,t=3,p=4$bjJxemdpaTZhamYwMDAwMA$mQp7+d3cVuwTx71lb6TrqiZU/lB3Ph8l4K5okmS5f9oeWPzk/lzR6jAJ2ozWuxhIGSQ6iy3O8tnLRB7QcZ0O0g', '5e8b7092-6ee2-47df-b24a-b3c9f733a9c4'),
    ('a3dc5f62-78f7-4215-b78a-cf75a6348067', 'esteban_gaviria@ejemplo.com', '$argon2id$v=19$m=64,t=3,p=4$bjJxemdpaTZhamYwMDAwMA$mQp7+d3cVuwTx71lb6TrqiZU/lB3Ph8l4K5okmS5f9oeWPzk/lzR6jAJ2ozWuxhIGSQ6iy3O8tnLRB7QcZ0O0g', '5e8b7092-6ee2-47df-b24a-b3c9f733a9c4'),
    ('e7c5d8a9-7c1f-4b5a-9d3a-6f8a7b2c4d0e', 'juan_diaz@ejemplo.com', '$argon2id$v=19$m=64,t=3,p=4$bjJxemdpaTZhamYwMDAwMA$mQp7+d3cVuwTx71lb6TrqiZU/lB3Ph8l4K5okmS5f9oeWPzk/lzR6jAJ2ozWuxhIGSQ6iy3O8tnLRB7QcZ0O0g', '5e8b7092-6ee2-47df-b24a-b3c9f733a9c4'),
    ('b4a7c8d9-e0f1-4a5b-9c8d-7e6f5a4b3c2d', 'miguel_gonzalez@ejemplo.com', '$argon2id$v=19$m=64,t=3,p=4$bjJxemdpaTZhamYwMDAwMA$mQp7+d3cVuwTx71lb6TrqiZU/lB3Ph8l4K5okmS5f9oeWPzk/lzR6jAJ2ozWuxhIGSQ6iy3O8tnLRB7QcZ0O0g', '5e8b7092-6ee2-47df-b24a-b3c9f733a9c4'),
    ('c8d9e0f1-a2b3-4c5d-6e7f-8a9b0c1d2e3f', 'pablo_pineda@ejemplo.com', '$argon2id$v=19$m=64,t=3,p=4$bjJxemdpaTZhamYwMDAwMA$mQp7+d3cVuwTx71lb6TrqiZU/lB3Ph8l4K5okmS5f9oeWPzk/lzR6jAJ2ozWuxhIGSQ6iy3O8tnLRB7QcZ0O0g', '5e8b7092-6ee2-47df-b24a-b3c9f733a9c4');

-- Inserción de perfiles (compatible con ambas versiones)
INSERT INTO profiles (id, full_name, username, streak, best_streak, avatar_url, created_at)
VALUES
    ('05e8c0f7-6433-4e23-ab06-36a67c7e23a3', 'Administrador', 'admin', 15, 20, 'https://randomuser.me/api/portraits/men/22.jpg', '2024-01-15 08:30:00'),
    ('8c7e5a5d-6eb8-4f21-b11c-2f7c31b38d15', 'Juan Colonia','juan_colonia', 3, 8, 'https://randomuser.me/api/portraits/men/35.jpg', '2024-02-10 14:45:00'),
    ('a3dc5f62-78f7-4215-b78a-cf75a6348067', 'Esteban Gaviria', 'esteban_gaviria', 7, 12, 'https://randomuser.me/api/portraits/men/68.jpg', '2023-11-22 11:15:00'),
    ('e7c5d8a9-7c1f-4b5a-9d3a-6f8a7b2c4d0e', 'Juan Diaz', 'juan_diaz', 0, 5, 'https://randomuser.me/api/portraits/men/42.jpg', '2024-03-05 09:20:00'),
    ('b4a7c8d9-e0f1-4a5b-9c8d-7e6f5a4b3c2d', 'Miguel Gonzalez', 'miguel_gonzalez', 22, 30, 'https://randomuser.me/api/portraits/men/15.jpg', '2023-10-18 16:50:00'),
    ('c8d9e0f1-a2b3-4c5d-6e7f-8a9b0c1d2e3f', 'Pablo Pineda', 'pablo_pineda', 9, 14, 'https://randomuser.me/api/portraits/men/19.jpg', '2024-01-30 12:10:00');

-- Permisos para usuarios públicos (sin autenticar)
INSERT INTO directus_permissions (role, collection, action, fields, permissions)
VALUES
    ((SELECT id FROM directus_roles WHERE name = 'Public'), 'directus_users', 'create', '*', '{}'),
    ((SELECT id FROM directus_roles WHERE name = 'Public'), 'directus_users', 'delete', '*', '{}'),
    ((SELECT id FROM directus_roles WHERE name = 'Public'), 'profiles', 'create', '*', '{}'),
    ((SELECT id FROM directus_roles WHERE name = 'Public'), 'habits', 'read', '*', '{}');

-- Permisos para el rol ADMIN (a21cfc5d-3f01-4e45-8e93-dd0d440af562)
-- Configuramos al administrador con acceso completo al sistema
UPDATE directus_roles 
SET admin_access = true, app_access = true
WHERE id = 'a21cfc5d-3f01-4e45-8e93-dd0d440af562';

-- En algunas versiones anteriores, la configuración admin_access = true otorga todos los permisos
-- Sin embargo, para mayor seguridad, añadimos permisos explícitos también
INSERT INTO directus_permissions (role, collection, action, fields, permissions)
VALUES
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'profiles', 'read', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'profiles', 'create', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'profiles', 'update', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'profiles', 'delete', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'habits', 'read', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'habits', 'create', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'habits', 'update', 'is_deleted', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'habits', 'delete', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'articles', 'read', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'articles', 'create', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'articles', 'update', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'articles', 'delete', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'articles_liked', 'read', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'articles_liked', 'create', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'articles_liked', 'delete', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'categories', 'read','*','{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'habits_days', 'read', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'habits_days', 'create', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'habits_days', 'update', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'habits_days', 'delete', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'habits_tracking', 'read', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'habits_tracking', 'create', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'habits_tracking', 'update', '*', '{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'categories', 'update','*','{}'),
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'active_user_habits', 'read', '*', '{}'),
    -- Añadimos permiso para la vista user_habit_calendar_view (sin filtro para admins)
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'user_habit_calendar_view', 'read', '*', '{}'),
    -- Añadimos permiso para la vista user_articles_view (sin filtro para admins)
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'user_articles_view', 'read', '*', '{}'),
    -- Permiso para leer la vista user_habit_tracking_view (sin filtro para admins)
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'user_habit_tracking_view', 'read', '*', '{"_and":[{"user_id":{"_eq":"$CURRENT_USER"}}]}'),
    -- Permiso para leer la vista ranked_articles_view (sin filtro para admins)
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'ranked_articles_view', 'read', '*', '{}');
    -- Permiso para leer la vista user_habit_categories_view (sin filtro para admins)
    ('a21cfc5d-3f01-4e45-8e93-dd0d440af562', 'user_habit_categories_view', 'read', '*', '{"_and":[{"user_id":{"_eq":"$CURRENT_USER"}}]}');


-- Permisos para el rol USER (5e8b7092-6ee2-47df-b24a-b3c9f733a9c4)
-- Configuramos el acceso básico para usuarios normales
UPDATE directus_roles 
SET admin_access = false, app_access = true
WHERE id = '5e8b7092-6ee2-47df-b24a-b3c9f733a9c4';

-- Añadimos permisos específicos para el rol USER
INSERT INTO directus_permissions (role, collection, action, fields, permissions)
VALUES
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'profiles', 'read', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'profiles', 'create', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'profiles', 'update', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'profiles', 'delete', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'habits', 'read', '*', '{"_and":[{"user_id":{"_eq":"$CURRENT_USER"}}]}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'habits', 'create', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'habits', 'update', 'is_deleted', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'habits', 'delete', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'articles', 'read', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'articles', 'create', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'articles', 'update', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'articles', 'delete', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'articles_liked', 'read', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'articles_liked', 'create', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'articles_liked', 'delete', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'categories', 'read','*','{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'habits_days', 'read', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'habits_days', 'create', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'habits_days', 'update', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'habits_days', 'delete', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'habits_tracking', 'read', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'habits_tracking', 'create', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'habits_tracking', 'update', '*', '{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'categories', 'update','*','{}'),
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'active_user_habits', 'read', '*', '{"_and":[{"user_id":{"_eq":"$CURRENT_USER"}}]}'),
    -- Permiso para leer la vista user_habit_calendar_view con filtro para el usuario actual
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'user_habit_calendar_view', 'read', '*', '{"_and":[{"user_id":{"_eq":"$CURRENT_USER"}}]}'),
    -- Permiso para leer la vista user_articles_view con filtro para el usuario actual
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'user_articles_view', 'read', '*', '{"_and":[{"user_id":{"_eq":"$CURRENT_USER"}}]}'),
    -- Permiso para leer la vista user_habit_tracking_view con filtro para el usuario actual
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'user_habit_tracking_view', 'read', '*', '{"_and":[{"user_id":{"_eq":"$CURRENT_USER"}}]}'),
    -- Permiso para leer la vista ranked_articles_view con filtro para el usuario actual
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'ranked_articles_view', 'read', '*', '{}');
    -- Permiso para leer la vista user_habit_categories_view con filtro para el usuario actual
    ('5e8b7092-6ee2-47df-b24a-b3c9f733a9c4', 'user_habit_categories_view', 'read', '*', '{"_and":[{"user_id":{"_eq":"$CURRENT_USER"}}]}');

-- Inserción de artículos
INSERT INTO articles (id, title, content, image_url, is_deleted, created_at, user_id, category_id)
VALUES
    ('f57ab8c9-d6e0-4f12-9ae0-5b8c4c1d2e3f', '5 Hábitos para mejorar tu salud mental', 'La salud mental es tan importante como la física. En este artículo exploraremos cinco hábitos cotidianos que pueden ayudarte a mantener un equilibrio mental saludable. Primero, establece una rutina de sueño regular, intentando dormir entre 7-8 horas cada noche. Segundo, practica la meditación o mindfulness durante al menos 10 minutos diarios. Tercero, mantén un diario donde expresar tus pensamientos y emociones. Cuarto, realiza actividad física moderada regularmente, ya que libera endorfinas que mejoran el estado de ánimo. Quinto, limita el uso de dispositivos electrónicos, especialmente antes de dormir.', 'https://images.unsplash.com/photo-1507120410856-1f35574c3b45', FALSE, '2024-01-20 09:15:00', 'a3dc5f62-78f7-4215-b78a-cf75a6348067', '1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed'),
    ('a7c9e0f1-b2d3-4e5f-6a7b-8c9d0e1f2a3b', 'Técnica Pomodoro: Aumenta tu productividad', 'La técnica Pomodoro es un método de gestión del tiempo desarrollado por Francesco Cirillo que utiliza intervalos de trabajo de 25 minutos separados por breves descansos. Para implementarla: 1) Elige una tarea a realizar, 2) Configura un temporizador para 25 minutos, 3) Trabaja en la tarea hasta que suene el temporizador, 4) Toma un descanso de 5 minutos, 5) Después de cuatro ciclos, toma un descanso más largo de 15-30 minutos. Esta técnica ayuda a mantener la concentración, reduce la fatiga mental y mejora la calidad del trabajo realizado.', 'https://images.unsplash.com/photo-1513128034602-7814ccaddd4e', FALSE, '2024-02-05 14:30:00', 'b4a7c8d9-e0f1-4a5b-9c8d-7e6f5a4b3c2d', '6ecd8c99-4036-403d-bf84-cf8400f67836'),
    ('d1e2f3a4-b5c6-7d8e-9f0a-1b2c3d4e5f6a', 'Guía para comenzar a meditar', 'La meditación es una práctica milenaria que ofrece numerosos beneficios para la salud física y mental. Para comenzar: 1) Encuentra un lugar tranquilo donde no serás interrumpido, 2) Siéntate en una posición cómoda, con la espalda recta, 3) Cierra los ojos y enfócate en tu respiración, 4) Cuando tu mente divague, gentilmente vuelve a prestar atención a tu respiración, 5) Comienza con sesiones de 5 minutos e incrementa gradualmente. La consistencia es más importante que la duración. Recuerda que es normal que tu mente se distraiga; el objetivo no es detener tus pensamientos sino observarlos sin juzgar.', 'https://images.unsplash.com/photo-1508672019048-805c876b67e2', FALSE, '2024-02-15 17:45:00', '05e8c0f7-6433-4e23-ab06-36a67c7e23a3', '7d7cfd2a-86a3-4879-aa4f-2e5413b3538b'),
    ('b8c9d0e1-f2a3-4b5c-6d7e-8f9a0b1c2d3e', 'Los beneficios del ayuno intermitente', 'El ayuno intermitente es un patrón de alimentación que alterna períodos de ingesta calórica con períodos de ayuno. Existen diferentes métodos como el 16/8 (16 horas de ayuno, 8 horas de alimentación) o el 5:2 (5 días normales, 2 días de restricción calórica). Entre sus beneficios se incluyen: pérdida de peso, mejora de la sensibilidad a la insulina, reducción de la inflamación, promoción de la autofagia celular y posible aumento de la longevidad. Sin embargo, no es adecuado para todos, y se recomienda consultar con un profesional de la salud antes de comenzar, especialmente si tienes condiciones médicas preexistentes.', 'https://images.unsplash.com/photo-1505576399279-565b52d4ac71', FALSE, '2024-03-10 10:20:00', 'c8d9e0f1-a2b3-4c5d-6e7f-8a9b0c1d2e3f', 'd2546fcd-39d8-4370-b34c-87d472a7a757'),
    ('e5f6a7b8-c9d0-1e2f-3a4b-5c6d7e8f9a0b', 'Cómo establecer un fondo de emergencia', 'Un fondo de emergencia es crucial para la estabilidad financiera. Idealmente, debería cubrir entre 3 y 6 meses de gastos básicos. Para crearlo: 1) Analiza tus gastos mensuales esenciales, 2) Establece una meta realista, 3) Configura transferencias automáticas a una cuenta separada, 4) Busca una cuenta de ahorro con buen rendimiento pero fácil acceso, 5) Utiliza ingresos inesperados (reembolsos, bonos) para aumentar tu fondo más rápidamente. Recuerda que este dinero debe usarse solo para verdaderas emergencias como problemas médicos, reparaciones urgentes o pérdida de empleo, no para gastos discrecionales.', 'https://images.unsplash.com/photo-1579621970563-ebec7560ff3e', FALSE, '2024-03-22 15:55:00', 'a3dc5f62-78f7-4215-b78a-cf75a6348067', '98f1c86b-8fec-4d71-b95e-da7f2e4c0ff3'),
    ('a1b2c3d4-e5f6-a7b8-c9d0-e1f2a3b4c5d6', 'Rutina de ejercicios en casa sin equipamiento', 'Mantenerse en forma no requiere una membresía de gimnasio costosa. Esta rutina de 30 minutos puede realizarse en cualquier espacio: 1) Calentamiento (5 min): Marcha en el sitio, rotaciones de brazos y tobillos, 2) Circuito principal (20 min): 45 segundos de ejercicio, 15 segundos de descanso - Sentadillas, flexiones modificadas, zancadas alternadas, plancha, saltos de tijera, 3) Enfriamiento (5 min): Estiramientos suaves para piernas, brazos y espalda. Realiza este circuito 3-4 veces por semana para ver resultados. Recuerda mantener una buena postura y respiración adecuada durante todos los ejercicios.', 'https://images.unsplash.com/photo-1517836357463-d25dfeac3438', FALSE, '2024-04-05 08:40:00', '8c7e5a5d-6eb8-4f21-b11c-2f7c31b38d15', '3e7c78bc-e145-4c2d-aa36-58b97f68a6d3');

-- Inserción de artículos guardados y me gusta
INSERT INTO articles_saved (user_id, article_id)
VALUES
    ('05e8c0f7-6433-4e23-ab06-36a67c7e23a3', 'a7c9e0f1-b2d3-4e5f-6a7b-8c9d0e1f2a3b'), -- Administrador guardó el artículo sobre Pomodoro
    ('05e8c0f7-6433-4e23-ab06-36a67c7e23a3', 'b8c9d0e1-f2a3-4b5c-6d7e-8f9a0b1c2d3e'), -- Administrador guardó el artículo sobre ayuno intermitente
    ('8c7e5a5d-6eb8-4f21-b11c-2f7c31b38d15', 'f57ab8c9-d6e0-4f12-9ae0-5b8c4c1d2e3f'), -- Juan Colonia guardó el artículo sobre salud mental
    ('a3dc5f62-78f7-4215-b78a-cf75a6348067', 'a1b2c3d4-e5f6-a7b8-c9d0-e1f2a3b4c5d6'), -- Esteban Gaviria guardó el artículo sobre ejercicios en casa
    ('b4a7c8d9-e0f1-4a5b-9c8d-7e6f5a4b3c2d', 'd1e2f3a4-b5c6-7d8e-9f0a-1b2c3d4e5f6a'); -- Juan Diaz guardó el artículo sobre meditación

INSERT INTO articles_liked (user_id, article_id)
VALUES
    ('05e8c0f7-6433-4e23-ab06-36a67c7e23a3', 'f57ab8c9-d6e0-4f12-9ae0-5b8c4c1d2e3f'), -- Administrador dio like al artículo sobre salud mental
    ('05e8c0f7-6433-4e23-ab06-36a67c7e23a3', 'a7c9e0f1-b2d3-4e5f-6a7b-8c9d0e1f2a3b'), -- Administrador dio like al artículo sobre Pomodoro
    ('8c7e5a5d-6eb8-4f21-b11c-2f7c31b38d15', 'f57ab8c9-d6e0-4f12-9ae0-5b8c4c1d2e3f'), -- Juan Colonia dio like al artículo sobre salud mental
    ('8c7e5a5d-6eb8-4f21-b11c-2f7c31b38d15', 'a1b2c3d4-e5f6-a7b8-c9d0-e1f2a3b4c5d6'), -- Juan Colonia dio like al artículo sobre ejercicios en casa
    ('a3dc5f62-78f7-4215-b78a-cf75a6348067', 'b8c9d0e1-f2a3-4b5c-6d7e-8f9a0b1c2d3e'), -- Esteban Gaviria dio like al artículo sobre ayuno intermitente
    ('b4a7c8d9-e0f1-4a5b-9c8d-7e6f5a4b3c2d', 'd1e2f3a4-b5c6-7d8e-9f0a-1b2c3d4e5f6a'), -- Juan Diaz dio like al artículo sobre meditación
    ('c8d9e0f1-a2b3-4c5d-6e7f-8a9b0c1d2e3f', 'e5f6a7b8-c9d0-1e2f-3a4b-5c6d7e8f9a0b'); -- Pablo Pineda dio like al artículo sobre fondo de emergencia

-- Inserción de hábitos
INSERT INTO habits (id, name, streak, notifications_enable, reminder_time, is_deleted, created_at, expiration_date, category_id, user_id)
VALUES
    ('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', 'Meditar 10 minutos', 8, TRUE, '08:00:00', FALSE, '2024-01-10 07:30:00', '2024-07-10', '7d7cfd2a-86a3-4879-aa4f-2e5413b3538b', '05e8c0f7-6433-4e23-ab06-36a67c7e23a3'),
    ('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', 'Beber 2 litros de agua', 4, TRUE, '07:30:00', FALSE, '2024-02-15 08:00:00', '2024-08-15', '1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed', '05e8c0f7-6433-4e23-ab06-36a67c7e23a3'),
    ('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', 'Leer 30 minutos', 2, FALSE, NULL, FALSE, '2024-03-01 21:15:00', '2024-09-01', 'b5a9f41d-31a6-4f11-b80f-7d2764c0a75b', '8c7e5a5d-6eb8-4f21-b11c-2f7c31b38d15'),
    ('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', 'Hacer ejercicio', 6, TRUE, '07:30:00', FALSE, '2024-02-20 17:00:00', '2024-08-20', '3e7c78bc-e145-4c2d-aa36-58b97f68a6d3', 'a3dc5f62-78f7-4215-b78a-cf75a6348067'),
    ('5e6f7a8b-9c0d-1e2f-3a4b-5c6d7e8f9a0b', 'Planificar comidas semanales', 3, TRUE, '07:30:00', FALSE, '2024-03-15 18:30:00', '2024-09-15', 'd2546fcd-39d8-4370-b34c-87d472a7a757', 'b4a7c8d9-e0f1-4a5b-9c8d-7e6f5a4b3c2d'),
    ('6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c', 'Escribir diario de gratitud', 10, TRUE, '07:30:00', FALSE, '2024-01-05 22:00:00', '2024-07-05', '7d7cfd2a-86a3-4879-aa4f-2e5413b3538b', 'c8d9e0f1-a2b3-4c5d-6e7f-8a9b0c1d2e3f'),
    ('7a8b9c0d-1e2f-3a4b-5c6d-7e8f9a0b1c2d', 'Ahorrar 10% del sueldo', 2, FALSE, NULL, FALSE, '2024-04-01 09:45:00', '2024-10-01', '98f1c86b-8fec-4d71-b95e-da7f2e4c0ff3', 'e7c5d8a9-7c1f-4b5a-9d3a-6f8a7b2c4d0e');

-- Inserción en habits_days
INSERT INTO habits_days (habit_id, week_day_id)
VALUES
    ('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', 'd3b15c58-711f-40d9-9f4b-06d0d6e925d1'), -- Meditar: Lunes
    ('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', 'b9b3995e-c6a5-46c7-bf8a-f1c1c2e65dd6'), -- Meditar: Martes
    ('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '4afe91c2-9851-4af7-b282-39a543989ea3'), -- Meditar: Miércoles
    ('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', 'ea5e7c7a-182c-4b49-8b2d-2162cd138384'), -- Meditar: Jueves
    ('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '22f2bf21-fcbd-473f-98d2-96ba47fabe16'), -- Meditar: Viernes
    ('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', 'd3b15c58-711f-40d9-9f4b-06d0d6e925d1'), -- Beber agua: Lunes
    ('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', 'b9b3995e-c6a5-46c7-bf8a-f1c1c2e65dd6'), -- Beber agua: Martes
    ('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', '4afe91c2-9851-4af7-b282-39a543989ea3'), -- Beber agua: Miércoles
    ('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', 'ea5e7c7a-182c-4b49-8b2d-2162cd138384'), -- Beber agua: Jueves
    ('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', '22f2bf21-fcbd-473f-98d2-96ba47fabe16'), -- Beber agua: Viernes
    ('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', 'f31a5698-2a4d-4818-8a0b-e7f843b9ec14'), -- Beber agua: Sábado
    ('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', '82a4b1c9-72a8-4e91-aaa4-2c92d30b810f'), -- Beber agua: Domingo
    ('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', 'ea5e7c7a-182c-4b49-8b2d-2162cd138384'), -- Leer: Jueves
    ('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', '22f2bf21-fcbd-473f-98d2-96ba47fabe16'), -- Leer: Viernes
    ('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', 'f31a5698-2a4d-4818-8a0b-e7f843b9ec14'), -- Leer: Sábado
    ('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', '82a4b1c9-72a8-4e91-aaa4-2c92d30b810f'), -- Leer: Domingo
    ('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', 'd3b15c58-711f-40d9-9f4b-06d0d6e925d1'), -- Ejercicio: Lunes
    ('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '4afe91c2-9851-4af7-b282-39a543989ea3'), -- Ejercicio: Miércoles
    ('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '22f2bf21-fcbd-473f-98d2-96ba47fabe16'), -- Ejercicio: Viernes
    ('5e6f7a8b-9c0d-1e2f-3a4b-5c6d7e8f9a0b', '82a4b1c9-72a8-4e91-aaa4-2c92d30b810f'), -- Planificar comidas: Domingo
    ('6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c', 'd3b15c58-711f-40d9-9f4b-06d0d6e925d1'), -- Diario gratitud: Lunes
    ('6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c', 'b9b3995e-c6a5-46c7-bf8a-f1c1c2e65dd6'), -- Diario gratitud: Martes
    ('6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c', '4afe91c2-9851-4af7-b282-39a543989ea3'), -- Diario gratitud: Miércoles
    ('6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c', 'ea5e7c7a-182c-4b49-8b2d-2162cd138384'), -- Diario gratitud: Jueves
    ('6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c', '22f2bf21-fcbd-473f-98d2-96ba47fabe16'), -- Diario gratitud: Viernes
    ('6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c', 'f31a5698-2a4d-4818-8a0b-e7f843b9ec14'), -- Diario gratitud: Sábado
    ('6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c', '82a4b1c9-72a8-4e91-aaa4-2c92d30b810f'), -- Diario gratitud: Domingo
    ('7a8b9c0d-1e2f-3a4b-5c6d-7e8f9a0b1c2d', 'd3b15c58-711f-40d9-9f4b-06d0d6e925d1'); -- Ahorrar: Lunes (día de la paga)

-- Inserción de seguimiento de hábitos
INSERT INTO habits_tracking (id, is_checked, tracking_date, habit_id)
VALUES
    ('a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d', TRUE, '2024-04-15', '1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d'), -- Meditar: Lunes pasado
    ('b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e', TRUE, '2024-04-16', '1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d'), -- Meditar: Martes pasado
    ('c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', TRUE, '2024-04-17', '1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d'), -- Meditar: Miércoles pasado
    ('d4e5f6a7-b8c9-0d1e-2f3a-4b5c6d7e8f9a', TRUE, '2024-04-18', '1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d'), -- Meditar: Jueves pasado
    ('e5f6a7b8-c9d0-1e2f-3a4b-5c6d7e8f9a0c', TRUE, '2024-04-19', '1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d'), -- Meditar: Viernes pasado
    ('f6a7b8c9-d0e1-2f3a-4b5c-6d7e8f9a0b1c', TRUE, '2024-04-15', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e'), -- Beber agua: Lunes pasado
    ('a7b8c9d0-e1f2-3a4b-5c6d-7e8f9a0b1c2d', TRUE, '2024-04-16', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e'), -- Beber agua: Martes pasado
    ('b8c9d0e1-f2a3-4b5c-6d7e-8f9a0b1c2d3e', FALSE, '2024-04-17', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e'), -- Beber agua: Miércoles pasado (no completado)
    ('c9d0e1f2-a3b4-5c6d-7e8f-9a0b1c2d3e4f', TRUE, '2024-04-18', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e'), -- Beber agua: Jueves pasado
    ('d0e1f2a3-b4c5-6d7e-8f9a-0b1c2d3e4f5a', TRUE, '2024-04-19', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e'), -- Beber agua: Viernes pasado
    ('e1f2a3b4-c5d6-7e8f-9a0b-1c2d3e4f5a6b', FALSE, '2024-04-20', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e'), -- Beber agua: Sábado pasado (no completado)
    ('f2a3b4c5-d6e7-8f9a-0b1c-2d3e4f5a6b7c', TRUE, '2024-04-21', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e'), -- Beber agua: Domingo pasado
    ('a3b4c5d6-e7f8-9a0b-1c2d-3e4f5a6b7c8d', FALSE, '2024-04-18', '3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f'), -- Leer: Jueves pasado (no completado)
    ('b4c5d6e7-f8a9-0b1c-2d3e-4f5a6b7c8d9e', TRUE, '2024-04-19', '3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f'), -- Leer: Viernes pasado
    ('c5d6e7f8-a9b0-1c2d-3e4f-5a6b7c8d9e0f', TRUE, '2024-04-20', '3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f'), -- Leer: Sábado pasado
    ('d6e7f8a9-b0c1-2d3e-4f5a-6b7c8d9e0f1a', TRUE, '2024-04-21', '3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f'), -- Leer: Domingo pasado
    ('e7f8a9b0-c1d2-3e4f-5a6b-7c8d9e0f1a2b', TRUE, '2024-04-15', '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a'), -- Ejercicio: Lunes pasado
    ('f8a9b0c1-d2e3-4f5a-6b7c-8d9e0f1a2b3c', TRUE, '2024-04-17', '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a'), -- Ejercicio: Miércoles pasado
    ('a9b0c1d2-e3f4-5a6b-7c8d-9e0f1a2b3c4d', TRUE, '2024-04-19', '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a'), -- Ejercicio: Viernes pasado
    ('b0c1d2e3-f4a5-6b7c-8d9e-0f1a2b3c4d5e', TRUE, '2024-04-14', '5e6f7a8b-9c0d-1e2f-3a4b-5c6d7e8f9a0b'), -- Planificar comidas: Domingo anterior
    ('c1d2e3f4-a5b6-7c8d-9e0f-1a2b3c4d5e6f', TRUE, '2024-04-21', '5e6f7a8b-9c0d-1e2f-3a4b-5c6d7e8f9a0b'), -- Planificar comidas: Domingo pasado
    ('d2e3f4a5-b6c7-8d9e-0f1a-2b3c4d5e6f7a', TRUE, '2024-04-15', '6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c'), -- Diario gratitud: Lunes pasado
    ('e3f4a5b6-c7d8-9e0f-1a2b-3c4d5e6f7a8b', TRUE, '2024-04-16', '6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c'), -- Diario gratitud: Martes pasado
    ('f4a5b6c7-d8e9-0f1a-2b3c-4d5e6f7a8b9c', TRUE, '2024-04-17', '6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c'), -- Diario gratitud: Miércoles pasado
    ('a5b6c7d8-e9f0-1a2b-3c4d-5e6f7a8b9c0d', TRUE, '2024-04-18', '6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c'), -- Diario gratitud: Jueves pasado
    ('b6c7d8e9-f0a1-2b3c-4d5e-6f7a8b9c0d1e', TRUE, '2024-04-19', '6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c'), -- Diario gratitud: Viernes pasado
    ('c7d8e9f0-a1b2-3c4d-5e6f-7a8b9c0d1e2f', TRUE, '2024-04-20', '6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c'), -- Diario gratitud: Sábado pasado
    ('d8e9f0a1-b2c3-4d5e-6f7a-8b9c0d1e2f3a', TRUE, '2024-04-21', '6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c'), -- Diario gratitud: Domingo pasado
    ('e9f0a1b2-c3d4-5e6f-7a8b-9c0d1e2f3a4b', TRUE, '2024-04-15', '7a8b9c0d-1e2f-3a4b-5c6d-7e8f9a0b1c2d'); -- Ahorrar: Lunes pasado


-- Inserción de hábitos para el usuario esteban_gaviria@ejemplo.com
INSERT INTO habits (id, name, streak, notifications_enable, reminder_time, is_deleted, created_at, expiration_date, category_id, user_id)
VALUES
    ('8f9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c', 'Hacer ejercicio matutino', 12, TRUE, '07:00:00', FALSE, '2025-04-01 06:30:00', '2025-10-01', '3e7c78bc-e145-4c2d-aa36-58b97f68a6d3', 'a3dc5f62-78f7-4215-b78a-cf75a6348067'),
    ('9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d', 'Leer antes de dormir', 7, TRUE, '21:30:00', FALSE, '2025-04-15 20:00:00', '2025-11-15', 'b5a9f41d-31a6-4f11-b80f-7d2764c0a75b', 'a3dc5f62-78f7-4215-b78a-cf75a6348067'),
    ('0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e', 'Meditar 15 minutos', 5, TRUE, '08:30:00', FALSE, '2025-05-01 07:45:00', '2025-12-01', '7d7cfd2a-86a3-4879-aa4f-2e5413b3538b', 'a3dc5f62-78f7-4215-b78a-cf75a6348067'),
    ('1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f', 'Tomar vitaminas', 3, FALSE, NULL, FALSE, '2025-05-10 12:00:00', '2025-08-10', '1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed', 'a3dc5f62-78f7-4215-b78a-cf75a6348067'),
    ('2d3e4f5a-6b7c-8d9e-0f1a-2b3c4d5e6f7a', 'Escribir en diario', 15, TRUE, '22:00:00', FALSE, '2025-03-20 21:30:00', '2025-09-20', 'b5a9f41d-31a6-4f11-b80f-7d2764c0a75b', 'a3dc5f62-78f7-4215-b78a-cf75a6348067');


-- Inserción en habits_days para programar los días de la semana
INSERT INTO habits_days (habit_id, week_day_id)
VALUES
    -- Ejercicio matutino: Lunes, Miércoles, Viernes
    ('8f9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c', 'd3b15c58-711f-40d9-9f4b-06d0d6e925d1'), -- Lunes
    ('8f9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c', '4afe91c2-9851-4af7-b282-39a543989ea3'), -- Miércoles
    ('8f9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c', '22f2bf21-fcbd-473f-98d2-96ba47fabe16'), -- Viernes
    
    -- Leer antes de dormir: Todos los días
    ('9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d', 'd3b15c58-711f-40d9-9f4b-06d0d6e925d1'), -- Lunes
    ('9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d', 'b9b3995e-c6a5-46c7-bf8a-f1c1c2e65dd6'), -- Martes
    ('9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d', '4afe91c2-9851-4af7-b282-39a543989ea3'), -- Miércoles
    ('9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d', 'ea5e7c7a-182c-4b49-8b2d-2162cd138384'), -- Jueves
    ('9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d', '22f2bf21-fcbd-473f-98d2-96ba47fabe16'), -- Viernes
    ('9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d', 'f31a5698-2a4d-4818-8a0b-e7f843b9ec14'), -- Sábado
    ('9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d', '82a4b1c9-72a8-4e91-aaa4-2c92d30b810f'), -- Domingo
    
    -- Meditar: Martes, Jueves, Sábado
    ('0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e', 'b9b3995e-c6a5-46c7-bf8a-f1c1c2e65dd6'), -- Martes
    ('0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e', 'ea5e7c7a-182c-4b49-8b2d-2162cd138384'), -- Jueves
    ('0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e', 'f31a5698-2a4d-4818-8a0b-e7f843b9ec14'), -- Sábado
    
    -- Tomar vitaminas: Lunes a Viernes
    ('1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f', 'd3b15c58-711f-40d9-9f4b-06d0d6e925d1'), -- Lunes
    ('1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f', 'b9b3995e-c6a5-46c7-bf8a-f1c1c2e65dd6'), -- Martes
    ('1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f', '4afe91c2-9851-4af7-b282-39a543989ea3'), -- Miércoles
    ('1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f', 'ea5e7c7a-182c-4b49-8b2d-2162cd138384'), -- Jueves
    ('1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f', '22f2bf21-fcbd-473f-98d2-96ba47fabe16'), -- Viernes
    
    -- Escribir en diario: Domingo
    ('2d3e4f5a-6b7c-8d9e-0f1a-2b3c4d5e6f7a', '82a4b1c9-72a8-4e91-aaa4-2c92d30b810f'); -- Domingo

-- Inserción de seguimiento de hábitos (basado en fecha actual: 24 de mayo de 2025)
-- Esto crea diferentes escenarios: días completados, parciales, ninguno y sin hábitos
INSERT INTO habits_tracking (id, is_checked, tracking_date, habit_id)
VALUES
    -- Semana del 19-25 de mayo de 2025 (semana actual)
    
    -- Lunes 19 mayo 2025 - DÍA COMPLETADO (todos los hábitos programados)
    ('3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8b', TRUE, '2025-05-19', '8f9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c'), -- Ejercicio ✓
    ('4f5a6b7c-8d9e-0f1a-2b3c-4d5e6f7a8b9c', TRUE, '2025-05-19', '9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d'), -- Leer ✓
    ('5a6b7c8d-9e0f-1a2b-3c4d-5e6f7a8b9c0d', TRUE, '2025-05-19', '1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f'), -- Vitaminas ✓
    
    -- Martes 20 mayo 2025 - DÍA PARCIAL (algunos hábitos completados)
    ('6b7c8d9e-0f1a-2b3c-4d5e-6f7a8b9c0d1e', TRUE, '2025-05-20', '9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d'), -- Leer ✓
    ('7c8d9e0f-1a2b-3c4d-5e6f-7a8b9c0d1e2f', TRUE, '2025-05-20', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e'), -- Meditar ✓
    ('8d9e0f1a-2b3c-4d5e-6f7a-8b9c0d1e2f3a', FALSE, '2025-05-20', '1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f'), -- Vitaminas ✗
    
    -- Miércoles 21 mayo 2025 - DÍA NINGUNO (ningún hábito completado)
    ('9e0f1a2b-3c4d-5e6f-7a8b-9c0d1e2f3a4b', FALSE, '2025-05-21', '8f9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c'), -- Ejercicio ✗
    ('0f1a2b3c-4d5e-6f7a-8b9c-0d1e2f3a4b5c', FALSE, '2025-05-21', '9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d'), -- Leer ✗
    ('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', FALSE, '2025-05-21', '1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f'), -- Vitaminas ✗
    
    -- Jueves 22 mayo 2025 - DÍA COMPLETADO
    ('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', TRUE, '2025-05-22', '9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d'), -- Leer ✓
    ('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', TRUE, '2025-05-22', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e'), -- Meditar ✓
    ('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', TRUE, '2025-05-22', '1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f'), -- Vitaminas ✓
    
    -- Viernes 23 mayo 2025 - DÍA PARCIAL
    ('5e6f7a8b-9c0d-1e2f-3a4b-5c6d7e8f9a0b', TRUE, '2025-05-23', '8f9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c'), -- Ejercicio ✓
    ('6f7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c', FALSE, '2025-05-23', '9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d'), -- Leer ✗
    ('7a8b9c0d-1e2f-3a4b-5c6d-7e8f9a0b1c2d', TRUE, '2025-05-23', '1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f'), -- Vitaminas ✓
    
    -- Sábado 24 mayo 2025 (hoy) - DÍA COMPLETADO
    ('8b9c0d1e-2f3a-4b5c-6d7e-8f9a0b1c2d3e', TRUE, '2025-05-24', '9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d'), -- Leer ✓
    ('9c0d1e2f-3a4b-5c6d-7e8f-9a0b1c2d3e4f', TRUE, '2025-05-24', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e'), -- Meditar ✓
    
    -- Domingo 25 mayo 2025 (mañana) - Sin registros aún, pero hay hábitos programados
    -- (No hay inserts para este día para mostrar el estado "sin registros pero con hábitos")
    
    -- Semana anterior (12-18 mayo 2025) - Para mostrar histórico
    
    -- Lunes 12 mayo 2025 - DÍA COMPLETADO
    ('0d1e2f3a-4b5c-6d7e-8f9a-0b1c2d3e4f5a', TRUE, '2025-05-12', '8f9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c'), -- Ejercicio ✓
    ('1e2f3a4b-5c6d-7e8f-9a0b-1c2d3e4f5a6b', TRUE, '2025-05-12', '9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d'), -- Leer ✓
    ('2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', TRUE, '2025-05-12', '1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f'), -- Vitaminas ✓
    
    -- Domingo 18 mayo 2025 - DÍA COMPLETADO (solo diario programado)
    ('3a4b5c6d-7e8f-9a0b-1c2d-3e4f5a6b7c8d', TRUE, '2025-05-18', '9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d'), -- Leer ✓
    ('4b5c6d7e-8f9a-0b1c-2d3e-4f5a6b7c8d9e', TRUE, '2025-05-18', '2d3e4f5a-6b7c-8d9e-0f1a-2b3c4d5e6f7a'), -- Diario ✓
    
    -- Días sin hábitos programados para mostrar estado "no hábitos"
    -- (Los días sin entries en habits_days mostrarán automáticamente "no hábitos")
    
    -- Algunos días más antiguos para mostrar variedad
    -- 5 mayo 2025 (domingo anterior) - PARCIAL
    ('5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e0f', TRUE, '2025-05-05', '9a0b1c2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d'), -- Leer ✓
    ('6d7e8f9a-0b1c-2d3e-4f5a-6b7c8d9e0f1a', FALSE, '2025-05-05', '2d3e4f5a-6b7c-8d9e-0f1a-2b3c4d5e6f7a'); -- Diario ✗

